package ai.nodeiq.transport

import ai.nodeiq.model.Query
import ai.nodeiq.model.StreamPart
import ai.nodeiq.mqtt.MqttClientManager
import ai.nodeiq.mqtt.MqttEvent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlinx.serialization.Serializable
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.jsonPrimitive
import kotlinx.serialization.json.buildJsonObject
import kotlinx.serialization.json.put
import timber.log.Timber
import java.util.UUID
import java.util.concurrent.atomic.AtomicBoolean

class MQTTTransport(
    private val clientManager: MqttClientManager,
    private val scope: CoroutineScope,
    private val json: Json = Json { ignoreUnknownKeys = true }
) : Transport {

    private val _events = MutableSharedFlow<P2PEvent>(extraBufferCapacity = 16, onBufferOverflow = BufferOverflow.DROP_OLDEST)
    override val events: SharedFlow<P2PEvent> = _events.asSharedFlow()

    private val responseMutex = Mutex()
    private val pendingResponses = mutableMapOf<String, Channel<StreamPart>>()
    private val started = AtomicBoolean(false)

    override suspend fun start() {
        if (!started.compareAndSet(false, true)) {
            return
        }
        Timber.i("Starting MQTT transport")
        clientManager.connect(clientIdSuffix = UUID.randomUUID().toString()) {
            subscribeTopics()
            scope.launch { _events.emit(P2PEvent.Ready) }
        }
        scope.launch {
            clientManager.events.collect { event ->
                when (event) {
                    is MqttEvent.Connected -> Timber.d("MQTT connected event")
                    is MqttEvent.Disconnected -> Timber.w("MQTT disconnected event")
                    is MqttEvent.MessageReceived -> handleMessage(event.topic, event.payload)
                }
            }
        }
    }

    private fun subscribeTopics() {
        val config = clientManager.topics
        clientManager.subscribe(config.presence, qos = 1)
    }

    override suspend fun advertise(agentId: String, skills: List<String>) {
        val payload = json.encodeToString(PresenceMessage(agentId = agentId, skills = skills))
        clientManager.subscribe(clientManager.topics.queryTopic(agentId), qos = 1)
        clientManager.publish(clientManager.topics.presence, payload, qos = 1, retained = true)
    }

    override suspend fun discover() {
        Timber.d("MQTT transport discover invoked - presence updates pushed via retained messages")
    }

    override suspend fun sendQuery(agentId: String, queryText: String) = flow {
        val queryId = UUID.randomUUID().toString()
        val fromPeer = "android-${UUID.randomUUID()}"
        val query = Query(queryId = queryId, fromPeer = fromPeer, agentId = agentId, queryText = queryText)
        val payload = json.encodeToString(query)

        val channel = Channel<StreamPart>(capacity = Channel.BUFFERED)
        responseMutex.withLock { pendingResponses[queryId] = channel }
        val responseTopic = clientManager.topics.responseTopic(queryId)
        clientManager.subscribe(responseTopic, qos = 1)
        clientManager.publish(clientManager.topics.queryTopic(agentId), payload, qos = 1, retained = false)

        try {
            for (part in channel) {
                emit(part)
                if (part is StreamPart.End || part is StreamPart.Error) {
                    break
                }
            }
        } finally {
            responseMutex.withLock { pendingResponses.remove(queryId) }
            if (!channel.isClosedForSend) {
                channel.close()
            }
        }
    }

    override suspend fun respond(queryId: String, parts: kotlinx.coroutines.flow.Flow<StreamPart>) {
        val topic = clientManager.topics.responseTopic(queryId)
        parts.collect { part ->
            when (part) {
                is StreamPart.Delta -> {
                    val payload = buildJsonObject {
                        put("version", "1.0")
                        put("type", "STREAM")
                        put("query_id", queryId)
                        put("seq", part.seq)
                        put("delta", part.delta)
                    }.toString()
                    clientManager.publish(topic, payload, qos = 1)
                    _events.emit(P2PEvent.StreamReceived(queryId, part.seq, part.delta))
                }
                is StreamPart.End -> {
                    val payload = buildJsonObject {
                        put("version", "1.0")
                        put("type", "END")
                        put("query_id", queryId)
                        put("status", part.status)
                    }.toString()
                    clientManager.publish(topic, payload, qos = 1)
                    _events.emit(P2PEvent.EndReceived(queryId, part.status))
                }
                is StreamPart.Error -> {
                    val payload = buildJsonObject {
                        put("version", "1.0")
                        put("type", "ERROR")
                        put("query_id", queryId)
                        put("error_text", part.message)
                    }.toString()
                    clientManager.publish(topic, payload, qos = 1)
                    _events.emit(P2PEvent.Error(part.message))
                }
            }
        }
    }

    private fun handleMessage(topic: String, payload: String) {
        val config = clientManager.topics
        when {
            topic == config.presence -> handlePresence(payload)
            topic.startsWith("/response/") -> handleResponse(payload)
            topic.startsWith("/query/") -> handleIncomingQuery(payload)
        }
    }

    private fun handlePresence(payload: String) {
        runCatching {
            val message = json.decodeFromString(PresenceMessage.serializer(), payload)
            scope.launch { _events.emit(P2PEvent.PeerDiscovered(peerId = message.peerId ?: message.agentId, agentId = message.agentId, skills = message.skills)) }
        }.onFailure { Timber.e(it, "Failed to parse presence payload: %s", payload) }
    }

    private fun handleIncomingQuery(payload: String) {
        runCatching {
            val node = json.parseToJsonElement(payload).jsonObject
            val queryId = node["query_id"]?.jsonPrimitive?.content ?: node["queryId"]?.jsonPrimitive?.content ?: return
            val fromPeer = node["from_peer"]?.jsonPrimitive?.content ?: node["fromPeer"]?.jsonPrimitive?.content ?: "unknown"
            val agentId = node["agent_id"]?.jsonPrimitive?.content ?: node["agentId"]?.jsonPrimitive?.content ?: ""
            val queryText = node["query_text"]?.jsonPrimitive?.content ?: node["queryText"]?.jsonPrimitive?.content ?: ""
            scope.launch { _events.emit(P2PEvent.QueryReceived(queryId, fromPeer, agentId, queryText)) }
        }.onFailure { Timber.e(it, "Failed to parse query payload: %s", payload) }
    }

    private fun handleResponse(payload: String) {
        runCatching {
            val node = json.parseToJsonElement(payload).jsonObject
            val queryId = node["query_id"]?.jsonPrimitive?.content ?: node["queryId"]?.jsonPrimitive?.content ?: return
            val type = node["type"]?.jsonPrimitive?.content ?: node["status"]?.jsonPrimitive?.content ?: "STREAM"
            val seq = node["seq"]?.jsonPrimitive?.content?.toIntOrNull() ?: 0
            val delta = node["delta"]?.jsonPrimitive?.content
            val status = node["status"]?.jsonPrimitive?.content ?: "STREAM"

            scope.launch {
                val channel = responseMutex.withLock { pendingResponses[queryId] }
                when (type) {
                    "STREAM", "Delta", "delta" -> {
                        channel?.send(StreamPart.Delta(seq = seq, delta = delta.orEmpty()))
                        _events.emit(P2PEvent.StreamReceived(queryId, seq, delta.orEmpty()))
                    }
                    "END", "End" -> {
                        channel?.let {
                            it.send(StreamPart.End(status = status))
                            if (!it.isClosedForSend) {
                                it.close()
                            }
                        }
                        _events.emit(P2PEvent.EndReceived(queryId, status))
                    }
                    else -> {
                        val errorText = node["error_text"]?.jsonPrimitive?.content ?: node["delta"]?.jsonPrimitive?.content ?: "Unknown"
                        channel?.let {
                            it.send(StreamPart.Error(errorText))
                            if (!it.isClosedForSend) {
                                it.close()
                            }
                        }
                        _events.emit(P2PEvent.Error(errorText))
                    }
                }
            }
        }.onFailure { Timber.e(it, "Failed to parse response payload: %s", payload) }
    }

    @Serializable
    private data class PresenceMessage(
        val agentId: String,
        val skills: List<String> = emptyList(),
        val peerId: String? = null
    )
}
