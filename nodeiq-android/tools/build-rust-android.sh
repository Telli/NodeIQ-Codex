#!/usr/bin/env bash
set -euo pipefail
cargo ndk -t arm64-v8a -o ../android/app/src/main/jniLibs build --manifest-path ../rust/ffi/Cargo.toml --release
