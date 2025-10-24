package ai.nodeiq.rag.index

class HnswIndex : VectorIndex {
    override suspend fun insert(docId: String, text: String, vector: FloatArray) {
        // TODO: integrate HNSW library
    }

    override suspend fun search(query: FloatArray, k: Int): List<Pair<String, Float>> {
        return emptyList()
    }
}
