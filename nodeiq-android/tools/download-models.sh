#!/usr/bin/env bash
set -euo pipefail
mkdir -p ../android/app/src/main/assets/models/embeddings
mkdir -p ../android/app/src/main/assets/models/llm
echo "Downloading MiniLM and tiny GGUF placeholders"
touch ../android/app/src/main/assets/models/embeddings/miniLM.onnx
touch ../android/app/src/main/assets/models/llm/tiny-q4_0.gguf
