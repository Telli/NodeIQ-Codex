# Architecture Overview

NodeIQ combines an Android application with on-device RAG, secure transports, and marketplace integrations. The Android layer orchestrates embeddings (ONNX Runtime), vector search (Room-backed cosine similarity), and token streaming via llama.cpp JNI bindings. Transport adapters support MQTT with mutual TLS and a feature-flagged libp2p stack implemented in Rust.
