package ai.nodeiq.store

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import ai.nodeiq.store.entities.MessageEntity
import ai.nodeiq.store.entities.PeerEntity

@Database(
    entities = [PeerEntity::class, MessageEntity::class],
    version = 1,
    exportSchema = false
)
@TypeConverters(SkillListConverter::class)
abstract class NodeIqDatabase : RoomDatabase() {
    abstract fun peerDao(): PeerDao
    abstract fun messageDao(): MessageDao
}
