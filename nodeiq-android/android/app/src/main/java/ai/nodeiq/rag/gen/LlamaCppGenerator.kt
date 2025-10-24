package ai.nodeiq.rag.gen

import android.content.Context
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers

class LlamaCppGenerator(
    private val context: Context,
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO
) : ILlmGenerator {

    init {
        runCatching { System.loadLibrary("nodeiq_llama") }
        nativeInit()
    }

    override fun load(modelPath: String, nCtx: Int): Boolean {
        // TODO connect to native bridge
        return true
    }

    override fun generateStream(
        prompt: String,
        maxTokens: Int,
        onToken: (String) -> Unit,
        onEnd: (String) -> Unit
    ) {
        onToken("stub-response")
        onEnd("SUCCESS")
    }

    private external fun nativeInit()
}
