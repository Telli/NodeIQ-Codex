package ai.nodeiq.store.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "peers")
data class PeerEntity(
    @PrimaryKey val peerId: String,
    val agentId: String,
    val skills: List<String>
)
