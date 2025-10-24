package ai.nodeiq.model

data class Peer(
    val id: String,
    val displayName: String,
    val skills: List<String>,
    val transport: String
)

data class Message(
    val id: String = java.util.UUID.randomUUID().toString(),
    val role: String,
    val text: String
)

data class LedgerEntry(
    val id: String,
    val peerId: String,
    val deltaCredits: Double,
    val description: String
)
