package ai.nodeiq.rag.gen

import android.content.Context
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers

class LlamaCppGenerator(
    private val context: Context,
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO
) : ILlmGenerator {

    private val libraryLoaded: Boolean

    init {
        libraryLoaded = runCatching { 
            System.loadLibrary("nodeiq_llama")
            true
        }.getOrDefault(false)
        
        if (libraryLoaded) {
            nativeInit()
        }
    }

    override fun load(modelPath: String, nCtx: Int): Boolean {
        if (!libraryLoaded) {
            return false
        }
        // TODO connect to native bridge
        return true
    }

    override fun generateStream(
        prompt: String,
        maxTokens: Int,
        onToken: (String) -> Unit,
        onEnd: (String) -> Unit
    ) {
        if (!libraryLoaded) {
            onEnd("ERROR: Native library not loaded")
            return
        }
        onToken("stub-response")
        onEnd("SUCCESS")
    }

    private external fun nativeInit()
}
