package ai.nodeiq.store

import ai.nodeiq.store.entities.MessageEntity
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface MessageDao {
    @Query("SELECT * FROM messages WHERE queryId = :queryId ORDER BY seq")
    fun streamMessages(queryId: String): Flow<List<MessageEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(message: MessageEntity)

    @Query("DELETE FROM messages WHERE queryId = :queryId")
    suspend fun clearQuery(queryId: String)
}
