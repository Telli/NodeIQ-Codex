package ai.nodeiq.transport

import ai.nodeiq.model.StreamPart
import ai.nodeiq.mqtt.MqttClientManager
import ai.nodeiq.mqtt.AgentConfig
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.SharedFlow
import timber.log.Timber

sealed class P2PEvent {
    object Ready : P2PEvent()
    data class PeerDiscovered(val peerId: String, val agentId: String, val skills: List<String>) : P2PEvent()
    data class QueryReceived(val queryId: String, val fromPeer: String, val agentId: String, val queryText: String) : P2PEvent()
    data class StreamReceived(val queryId: String, val seq: Int, val delta: String) : P2PEvent()
    data class EndReceived(val queryId: String, val status: String) : P2PEvent()
    data class Error(val message: String) : P2PEvent()
}

interface Transport {
    val events: SharedFlow<P2PEvent>
    suspend fun start()
    suspend fun advertise(agentId: String, skills: List<String>)
    suspend fun discover()
    suspend fun sendQuery(agentId: String, queryText: String): kotlinx.coroutines.flow.Flow<StreamPart>
    suspend fun respond(queryId: String, parts: kotlinx.coroutines.flow.Flow<StreamPart>)
}

class TransportRouter(
    private val mqttClientManager: MqttClientManager,
    private val coroutineScope: CoroutineScope = CoroutineScope(Dispatchers.IO)
) {

    private val mqttTransport by lazy { MQTTTransport(mqttClientManager, coroutineScope) }
    private val libp2pTransport by lazy { Lp2pTransport(coroutineScope) }

    private val _activeTransport: Transport by lazy {
        mqttClientManager.loadConfig()
        if (mqttClientManager.appConfig.featureFlags.transportMqtt) {
            Timber.i("Using MQTT transport")
            mqttTransport
        } else {
            Timber.i("MQTT disabled, falling back to libp2p transport")
            libp2pTransport
        }
    }

    val events: SharedFlow<P2PEvent> get() = _activeTransport.events

    val agentConfig: AgentConfig
        get() {
            mqttClientManager.loadConfig()
            return mqttClientManager.appConfig.agent
        }

    suspend fun start() = _activeTransport.start()

    suspend fun advertise(agentId: String, skills: List<String>) = _activeTransport.advertise(agentId, skills)

    suspend fun discover() = _activeTransport.discover()

    suspend fun sendQuery(agentId: String, queryText: String) = _activeTransport.sendQuery(agentId, queryText)

    suspend fun respond(queryId: String, parts: kotlinx.coroutines.flow.Flow<StreamPart>) =
        _activeTransport.respond(queryId, parts)
}
