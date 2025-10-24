package ai.nodeiq.rag.emb

import android.content.Context
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlin.math.sqrt

open class OnnxEmbedder(protected val context: Context) {
    open suspend fun embed(text: String): FloatArray = withContext(Dispatchers.IO) {
        val tokens = text.split(" ")
        val vector = FloatArray(128) { index -> (tokens.sumOf { it.length } + index).toFloat() }
        normalize(vector)
    }

    private fun normalize(vector: FloatArray): FloatArray {
        var sumSquares = 0f
        for (value in vector) {
            sumSquares += value * value
        }
        val norm = sqrt(sumSquares)
        if (norm == 0f) return vector
        for (i in vector.indices) {
            vector[i] /= norm
        }
        return vector
    }
}
