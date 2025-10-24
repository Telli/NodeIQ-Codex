package ai.nodeiq.store

import ai.nodeiq.store.entities.PeerEntity
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface PeerDao {
    @Query("SELECT * FROM peers ORDER BY agentId")
    fun observePeers(): Flow<List<PeerEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertPeers(peers: List<PeerEntity>)

    @Query("DELETE FROM peers")
    suspend fun clear()
}
