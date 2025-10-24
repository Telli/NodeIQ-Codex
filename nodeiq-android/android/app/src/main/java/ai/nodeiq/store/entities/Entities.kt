package ai.nodeiq.store.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "peers")
data class PeerEntity(
    @PrimaryKey val id: String,
    val displayName: String,
    val skills: String,
    val transport: String,
    val lastSeen: Long
)

@Entity(tableName = "messages")
data class MessageEntity(
    @PrimaryKey val id: String,
    val peerId: String,
    val role: String,
    val text: String,
    val createdAt: Long
)

@Entity(tableName = "docs")
data class DocEntity(
    @PrimaryKey val id: String,
    val text: String,
    @ColumnInfo(typeAffinity = ColumnInfo.BLOB) val vector: FloatArray
)

@Entity(tableName = "ledger")
data class LedgerEntity(
    @PrimaryKey val id: String,
    val peerId: String,
    val deltaCredits: Double,
    val description: String,
    val createdAt: Long
)
