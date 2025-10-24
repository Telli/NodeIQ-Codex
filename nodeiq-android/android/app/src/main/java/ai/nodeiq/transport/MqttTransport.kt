package ai.nodeiq.transport

import ai.nodeiq.config.AppConfig
import ai.nodeiq.mqtt.MqttClientManager
import ai.nodeiq.rag.RagEngine
import ai.nodeiq.store.NodeIqDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.eclipse.paho.client.mqttv3.IMqttClient

class MqttTransport(
    private val context: android.content.Context,
    private val config: AppConfig,
    private val ragEngine: RagEngine,
    private val db: NodeIqDatabase,
    private val clientManager: MqttClientManager
) : Transport {
    override val name: String = "MQTT"
    override val enabled: Boolean = config.feature_flags.transport_mqtt

    private val scope = CoroutineScope(Dispatchers.IO)
    private var client: IMqttClient? = null

    override suspend fun start() {
        val serverUri = "ssl://${config.mqtt.host}:${config.mqtt.port}"
        client = clientManager.createClient(serverUri)
        subscribeTopics()
        publishPresence()
    }

    override suspend fun stop() {
        client?.disconnect()
        client?.close()
        client = null
    }

    private fun subscribeTopics() {
        val queryTopic = config.topics.query.replace("{agent_id}", config.agent.agent_id)
        client?.subscribe(queryTopic) { _, message ->
            scope.launch {
                val payload = message.payload.toString(Charsets.UTF_8)
                // TODO parse query and stream response
            }
        }
    }

    private fun publishPresence() {
        val presencePayload = "{" + "\"agent_id\":\"${config.agent.agent_id}\"" + "}"
        client?.publish(config.topics.presence, presencePayload.toByteArray(), 1, true)
    }
}
