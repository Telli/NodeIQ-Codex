package ai.nodeiq.rag

import ai.nodeiq.rag.emb.OnnxEmbedder
import ai.nodeiq.rag.gen.ILlmGenerator
import ai.nodeiq.rag.index.VectorIndex
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertTrue
import org.junit.Test

class RagEngineTest {
    @Test
    fun `answerStream emits end`() = runBlocking {
        val engine = RagEngine(
            embedder = object : OnnxEmbedder(android.app.Application()) {
                override suspend fun embed(text: String): FloatArray = floatArrayOf(1f)
            },
            index = object : VectorIndex {
                override suspend fun insert(docId: String, text: String, vector: FloatArray) {}
                override suspend fun search(query: FloatArray, k: Int): List<Pair<String, Float>> = emptyList()
            },
            generator = object : ILlmGenerator {
                override fun load(modelPath: String, nCtx: Int): Boolean = true
                override fun generateStream(prompt: String, maxTokens: Int, onToken: (String) -> Unit, onEnd: (String) -> Unit) {
                    onEnd("SUCCESS")
                }
            }
        )

        val part = engine.answerStream("test").first()
        assertTrue(part is StreamPart.End)
    }
}
