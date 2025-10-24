package ai.nodeiq.rag.ingest

import ai.nodeiq.rag.emb.OnnxEmbedder
import ai.nodeiq.rag.index.VectorIndex
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class IngestService @Inject constructor(
    private val embedder: OnnxEmbedder,
    private val index: VectorIndex
) {
    private val scope = CoroutineScope(Dispatchers.IO)

    fun ingest(docId: String, text: String) {
        scope.launch {
            val vector = embedder.embed(text)
            index.insert(docId, text, vector)
        }
    }
}
