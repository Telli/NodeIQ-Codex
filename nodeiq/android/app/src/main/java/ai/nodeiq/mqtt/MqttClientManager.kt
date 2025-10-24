package ai.nodeiq.mqtt

import android.content.Context
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.launch
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import org.eclipse.paho.android.service.MqttAndroidClient
import org.eclipse.paho.client.mqttv3.DisconnectedBufferOptions
import org.eclipse.paho.client.mqttv3.IMqttActionListener
import org.eclipse.paho.client.mqttv3.IMqttToken
import org.eclipse.paho.client.mqttv3.MqttCallbackExtended
import org.eclipse.paho.client.mqttv3.MqttConnectOptions
import org.eclipse.paho.client.mqttv3.MqttMessage
import timber.log.Timber
import java.io.InputStreamReader
import java.security.KeyStore
import javax.net.ssl.KeyManagerFactory
import javax.net.ssl.SSLContext
import javax.net.ssl.TrustManagerFactory

@Serializable
data class FeatureFlags(
    @SerialName("transport_mqtt") val transportMqtt: Boolean = true,
    @SerialName("transport_libp2p") val transportLibp2p: Boolean = false
)

@Serializable
data class MqttConfig(
    val host: String,
    val port: Int,
    @SerialName("client_id_prefix") val clientIdPrefix: String,
    @SerialName("tls_enabled") val tlsEnabled: Boolean = true,
    @SerialName("ca_cert_asset") val caCertAsset: String,
    @SerialName("client_cert_asset") val clientCertAsset: String,
    @SerialName("client_key_asset") val clientKeyAsset: String,
    @SerialName("keep_alive_sec") val keepAliveSec: Int = 30,
    @SerialName("reconnect_backoff_sec") val reconnectBackoffSec: List<Int> = listOf(1, 3, 5, 10, 20)
)

@Serializable
data class Libp2pConfig(
    @SerialName("bootstrap_multiaddrs") val bootstrapMultiaddrs: List<String> = emptyList(),
    val namespace: String = "kmarket",
    @SerialName("listen_on") val listenOn: List<String> = emptyList(),
    @SerialName("enable_relay_client") val enableRelayClient: Boolean = true,
    @SerialName("discovery_interval_sec") val discoveryIntervalSec: Int = 30
)

@Serializable
data class AgentConfig(
    @SerialName("agent_id") val agentId: String,
    val skills: List<String> = emptyList()
)

@Serializable
data class AppConfig(
    @SerialName("feature_flags") val featureFlags: FeatureFlags,
    val mqtt: MqttConfig,
    val topics: TopicStrings,
    val libp2p: Libp2pConfig,
    val agent: AgentConfig
)

@Serializable
data class TopicStrings(
    val presence: String,
    val query: String,
    val response: String
)

sealed class MqttEvent {
    object Connected : MqttEvent()
    object Disconnected : MqttEvent()
    data class MessageReceived(val topic: String, val payload: String) : MqttEvent()
}

class MqttClientManager(private val context: Context) {

    private val scope = CoroutineScope(Dispatchers.IO)
    private val json = Json { ignoreUnknownKeys = true }

    private val _events = MutableSharedFlow<MqttEvent>(extraBufferCapacity = 8, onBufferOverflow = BufferOverflow.DROP_OLDEST)
    val events: SharedFlow<MqttEvent> = _events

    private var client: MqttAndroidClient? = null
    lateinit var appConfig: AppConfig
        private set

    lateinit var topics: TopicConfig
        private set

    fun loadConfig() {
        if (this::appConfig.isInitialized) return
        context.assets.open("appsettings.json").use { inputStream ->
            val jsonText = InputStreamReader(inputStream).readText()
            appConfig = json.decodeFromString(jsonText)
            topics = TopicConfig(
                presence = appConfig.topics.presence,
                queryTemplate = appConfig.topics.query,
                responseTemplate = appConfig.topics.response
            )
        }
    }

    fun connect(clientIdSuffix: String, scope: CoroutineScope = this.scope, onConnected: (() -> Unit)? = null) {
        loadConfig()
        val scheme = if (appConfig.mqtt.tlsEnabled) "ssl" else "tcp"
        val serverUri = "$scheme://${appConfig.mqtt.host}:${appConfig.mqtt.port}"
        val clientId = appConfig.mqtt.clientIdPrefix + clientIdSuffix
        val client = MqttAndroidClient(context, serverUri, clientId)
        client.setCallback(object : MqttCallbackExtended {
            override fun connectComplete(reconnect: Boolean, serverURI: String?) {
                Timber.i("Connected to MQTT broker: $serverURI")
                scope.launch { _events.emit(MqttEvent.Connected) }
                onConnected?.invoke()
            }

            override fun connectionLost(cause: Throwable?) {
                Timber.w(cause, "MQTT connection lost")
                scope.launch { _events.emit(MqttEvent.Disconnected) }
            }

            override fun messageArrived(topic: String?, message: MqttMessage?) {
                if (topic != null && message != null) {
                    val payload = message.payload.decodeToString()
                    Timber.d("MQTT message on %s: %s", topic, payload)
                    scope.launch { _events.emit(MqttEvent.MessageReceived(topic, payload)) }
                }
            }

            override fun deliveryComplete(token: IMqttToken?) {
                Timber.d("MQTT delivery complete: %s", token?.topics?.joinToString())
            }
        })

        val options = MqttConnectOptions().apply {
            isCleanSession = false
            keepAliveInterval = appConfig.mqtt.keepAliveSec
            connectionTimeout = 30
            userName = null
            if (appConfig.mqtt.tlsEnabled) {
                socketFactory = buildSslContext().socketFactory
            }
        }

        client.setBufferOpts(DisconnectedBufferOptions().apply {
            isBufferEnabled = true
            bufferSize = 100
            isDeleteOldestMessages = true
        })

        this.client = client
        Timber.i("Connecting to MQTT %s", serverUri)
        client.connect(options, null, object : IMqttActionListener {
            override fun onSuccess(asyncActionToken: IMqttToken?) {
                Timber.i("MQTT connected with clientId=%s", client.clientId)
            }

            override fun onFailure(asyncActionToken: IMqttToken?, exception: Throwable?) {
                Timber.e(exception, "MQTT connection failed")
            }
        })
    }

    private fun buildSslContext(): SSLContext {
        val cf = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm())
        val kf = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm())

        val trustStore = KeyStore.getInstance(KeyStore.getDefaultType()).apply {
            load(null)
            loadCertificate(appConfig.mqtt.caCertAsset)?.let { setCertificateEntry("ca", it) }
        }
        cf.init(trustStore)

        val keyStore = PemKeyStoreBuilder(context).build(
            certAsset = appConfig.mqtt.clientCertAsset,
            keyAsset = appConfig.mqtt.clientKeyAsset
        )
        kf.init(keyStore, CharArray(0))

        return SSLContext.getInstance("TLS").apply {
            init(kf.keyManagers, cf.trustManagers, null)
        }
    }

    private fun KeyStore.loadCertificate(assetName: String) = context.assets.open(assetName).use { input ->
        java.security.cert.CertificateFactory.getInstance("X.509").generateCertificate(input)
    }

    fun subscribe(topic: String, qos: Int = 1) {
        client?.subscribe(topic, qos, null, object : IMqttActionListener {
            override fun onSuccess(asyncActionToken: IMqttToken?) {
                Timber.i("Subscribed to %s", topic)
            }

            override fun onFailure(asyncActionToken: IMqttToken?, exception: Throwable?) {
                Timber.e(exception, "Failed to subscribe to %s", topic)
            }
        })
    }

    fun publish(topic: String, payload: String, qos: Int = 1, retained: Boolean = false) {
        val message = MqttMessage(payload.toByteArray()).apply {
            this.qos = qos
            isRetained = retained
        }
        client?.publish(topic, message)
    }

    fun disconnect() {
        client?.disconnect()
        client = null
    }
}

private class PemKeyStoreBuilder(private val context: Context) {
    fun build(certAsset: String, keyAsset: String): KeyStore {
        val certificateFactory = java.security.cert.CertificateFactory.getInstance("X.509")
        val cert = context.assets.open(certAsset).use { certificateFactory.generateCertificate(it) }
        val privateKey = context.assets.open(keyAsset).use { PemUtils.loadPrivateKey(it.readBytes()) }

        return KeyStore.getInstance("PKCS12").apply {
            load(null, CharArray(0))
            setKeyEntry("client", privateKey, CharArray(0), arrayOf(cert))
        }
    }
}

private object PemUtils {
    fun loadPrivateKey(data: ByteArray): java.security.PrivateKey {
        val parser = String(data)
            .replace("-----BEGIN PRIVATE KEY-----", "")
            .replace("-----END PRIVATE KEY-----", "")
            .replace("-----BEGIN RSA PRIVATE KEY-----", "")
            .replace("-----END RSA PRIVATE KEY-----", "")
            .replace("\n", "")
        val decoded = java.util.Base64.getDecoder().decode(parser)
        val keySpec = java.security.spec.PKCS8EncodedKeySpec(decoded)
        val kf = java.security.KeyFactory.getInstance("RSA")
        return kf.generatePrivate(keySpec)
    }
}
