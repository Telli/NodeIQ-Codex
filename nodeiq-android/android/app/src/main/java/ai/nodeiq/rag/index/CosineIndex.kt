package ai.nodeiq.rag.index

import ai.nodeiq.store.DocDao
import ai.nodeiq.store.entities.DocEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlin.math.sqrt

class CosineIndex(private val dao: DocDao) : VectorIndex {
    override suspend fun insert(docId: String, text: String, vector: FloatArray) {
        dao.upsert(DocEntity(id = docId, text = text, vector = vector))
    }

    override suspend fun search(query: FloatArray, k: Int): List<Pair<String, Float>> = withContext(Dispatchers.IO) {
        val docs = dao.all()
        docs.map { it.text to cosine(query, it.vector) }
            .sortedByDescending { it.second }
            .take(k)
    }

    private fun cosine(a: FloatArray, b: FloatArray): Float {
        val size = minOf(a.size, b.size)
        var dot = 0.0
        var aNorm = 0.0
        var bNorm = 0.0
        for (i in 0 until size) {
            dot += (a[i] * b[i])
            aNorm += (a[i] * a[i])
            bNorm += (b[i] * b[i])
        }
        val denom = sqrt(aNorm).toDouble() * sqrt(bNorm).toDouble()
        return if (denom == 0.0) 0f else (dot / denom).toFloat()
    }
}
