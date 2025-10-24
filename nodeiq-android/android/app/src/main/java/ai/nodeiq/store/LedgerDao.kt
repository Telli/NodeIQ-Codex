package ai.nodeiq.store

import ai.nodeiq.store.entities.LedgerEntity
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface LedgerDao {
    @Query("SELECT * FROM ledger ORDER BY createdAt DESC")
    fun observeLedger(): Flow<List<LedgerEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(entry: LedgerEntity)

    @Query("SELECT COALESCE(SUM(deltaCredits), 0) FROM ledger")
    suspend fun balance(): Double
}
