package ai.nodeiq.store.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "messages")
data class MessageEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val queryId: String,
    val seq: Int,
    val delta: String,
    val isFinal: Boolean
)
