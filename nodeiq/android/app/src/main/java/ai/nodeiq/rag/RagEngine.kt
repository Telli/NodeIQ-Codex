package ai.nodeiq.rag

import ai.nodeiq.model.StreamPart
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

object RagEngine {
    fun answerStream(prompt: String): Flow<StreamPart> = flow {
        val tokens = listOf(
            "The",
            " P0305",
            " code",
            " indicates",
            " a",
            " misfire",
            " on",
            " cylinder",
            " 5",
            "."
        )
        tokens.forEachIndexed { index, token ->
            emit(StreamPart.Delta(seq = index, delta = token))
            delay(100)
        }
        emit(StreamPart.End())
    }
}
