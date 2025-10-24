package ai.nodeiq.rag

import kotlinx.coroutines.flow.Flow

sealed interface StreamPart {
    val isTerminal: Boolean

    data class Delta(val seq: Int, val token: String) : StreamPart {
        override val isTerminal: Boolean = false
    }

    data class End(val status: String) : StreamPart {
        override val isTerminal: Boolean = true
    }
}

interface IRagEngine {
    fun answerStream(userQuery: String): Flow<StreamPart>
}
