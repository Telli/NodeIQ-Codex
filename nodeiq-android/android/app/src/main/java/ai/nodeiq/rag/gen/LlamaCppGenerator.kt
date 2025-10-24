package ai.nodeiq.rag.gen

import android.content.Context
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers

class LlamaCppGenerator(
    private val context: Context,
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO
) : ILlmGenerator {

    private val isNativeLibraryLoaded: Boolean

    init {
        isNativeLibraryLoaded = runCatching { 
            System.loadLibrary("nodeiq_llama")
            true
        }.getOrDefault(false)
        
        if (isNativeLibraryLoaded) {
            nativeInit()
        }
    }

    override fun load(modelPath: String, nCtx: Int): Boolean {
        if (!isNativeLibraryLoaded) {
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
        if (!isNativeLibraryLoaded) {
            provideStubResponse(onToken, onEnd)
            return
        }
        // TODO: call native bridge when library is loaded
        provideStubResponse(onToken, onEnd)
    }

    private fun provideStubResponse(onToken: (String) -> Unit, onEnd: (String) -> Unit) {
        onToken("stub-response")
        onEnd("SUCCESS")
    }

    private external fun nativeInit()
}
