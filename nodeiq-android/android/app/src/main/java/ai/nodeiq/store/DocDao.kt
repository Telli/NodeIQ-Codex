package ai.nodeiq.store

import ai.nodeiq.store.entities.DocEntity
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface DocDao {
    @Query("SELECT * FROM docs")
    fun observeDocs(): Flow<List<DocEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(doc: DocEntity)

    @Query("SELECT * FROM docs")
    suspend fun all(): List<DocEntity>
}
