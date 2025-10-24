# RAG Pipeline

1. Ingest content via `IngestService`, chunking and embedding with ONNX Runtime.
2. Store normalized vectors in Room (`DocDao`).
3. At query time, embed user text, search `VectorIndex`, and compose a prompt.
4. Stream llama.cpp tokens back to UI and transports.
