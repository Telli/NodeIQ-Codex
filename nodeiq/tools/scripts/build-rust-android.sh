#!/usr/bin/env bash
set -euo pipefail

ROOT_DIR="$(cd "$(dirname "$0")"/../.. && pwd)"
RUST_DIR="$ROOT_DIR/rust/ffi"
ANDROID_DIR="$ROOT_DIR/android/app/src/main/jniLibs/arm64-v8a"

cd "$RUST_DIR"

echo "Building Rust FFI for aarch64-linux-android"
cargo ndk -t arm64-v8a -o target/ndk build --release

mkdir -p "$ANDROID_DIR"
cp target/ndk/arm64-v8a/release/libnodeiq_p2p.so "$ANDROID_DIR"

echo "Updated libnodeiq_p2p.so"
