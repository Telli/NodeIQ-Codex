package ai.nodeiq.rag.gen

interface ILlmGenerator {
    fun load(modelPath: String, nCtx: Int = 2048): Boolean
    fun generateStream(prompt: String, maxTokens: Int, onToken: (String) -> Unit, onEnd: (String) -> Unit)
}
