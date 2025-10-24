package ai.nodeiq.model

import kotlinx.serialization.Serializable

@Serializable
data class PeerSummary(
    val peerId: String,
    val agentId: String,
    val skills: List<String> = emptyList()
)

@Serializable
data class Query(
    val version: String = "1.0",
    val type: String = "QUERY",
    val queryId: String,
    val fromPeer: String,
    val agentId: String,
    val queryText: String,
    val ttlMs: Int = 45_000
)

sealed interface StreamPart {
    @Serializable
    data class Delta(val seq: Int, val delta: String) : StreamPart

    @Serializable
    data class End(val status: String = "SUCCESS") : StreamPart

    @Serializable
    data class Error(val message: String) : StreamPart
}
