package ai.nodeiq.store

import ai.nodeiq.store.entities.DocEntity
import ai.nodeiq.store.entities.LedgerEntity
import ai.nodeiq.store.entities.MessageEntity
import ai.nodeiq.store.entities.PeerEntity
import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

@Database(
    entities = [PeerEntity::class, MessageEntity::class, DocEntity::class, LedgerEntity::class],
    version = 1,
    exportSchema = true
)
@TypeConverters(VectorConverters::class)
abstract class NodeIqDatabase : RoomDatabase() {
    abstract fun peerDao(): PeerDao
    abstract fun messageDao(): MessageDao
    abstract fun docDao(): DocDao
    abstract fun ledgerDao(): LedgerDao

    companion object {
        fun build(context: Context): NodeIqDatabase =
            Room.databaseBuilder(context, NodeIqDatabase::class.java, "nodeiq.db").fallbackToDestructiveMigration().build()
    }
}
