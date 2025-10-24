package ai.nodeiq.rag

import ai.nodeiq.rag.emb.OnnxEmbedder
import ai.nodeiq.rag.gen.ILlmGenerator
import ai.nodeiq.rag.index.VectorIndex
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.withContext

class RagEngine(
    private val embedder: OnnxEmbedder,
    private val index: VectorIndex,
    private val generator: ILlmGenerator,
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO
) : IRagEngine {
    override fun answerStream(userQuery: String): Flow<StreamPart> = flow {
        val queryVector = embedder.embed(userQuery)
        val hits = index.search(queryVector, k = 5)
        val prompt = buildPrompt(userQuery, hits)
        val channel = Channel<StreamPart>(Channel.UNLIMITED)
        var seq = 0
        withContext(dispatcher) {
            generator.generateStream(
                prompt = prompt,
                maxTokens = 256,
                onToken = { token -> channel.trySend(StreamPart.Delta(seq = seq++, token = token)) },
                onEnd = { status -> channel.trySend(StreamPart.End(status)) }
            )
        }
        for (part in channel) {
            emit(part)
            if (part is StreamPart.End) {
                break
            }
        }
    }.flowOn(Dispatchers.Default)

    private fun buildPrompt(userQuery: String, hits: List<Pair<String, Float>>): String {
        val context = hits.joinToString(separator = "\n\n") { (doc, score) -> "[score=$score]\n$doc" }
        return "$context\n\nUser: $userQuery\nAssistant:"
    }
}
