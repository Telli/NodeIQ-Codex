package ai.nodeiq.rag.index

interface VectorIndex {
    suspend fun insert(docId: String, text: String, vector: FloatArray)
    suspend fun search(query: FloatArray, k: Int): List<Pair<String, Float>>
}
