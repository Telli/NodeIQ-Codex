# NodeIQ Monorepo

NodeIQ is a decentralized knowledge network MVP optimized for mobile reliability. This repository bundles the Android application, Rust libp2p core, MQTT broker configuration, and supporting documentation.

## Prerequisites

- JDK 17 and Android Studio Flamingo or newer
- Android SDK/NDK with `cargo-ndk` installed (`cargo install cargo-ndk`)
- Rust toolchain (stable)
- Docker for running Mosquitto
- OpenSSL for certificate generation

## Quick Start

```bash
# Generate mutual TLS certificates
nodeiq/tools/scripts/gen-certs.sh

# Run Mosquitto broker
nodeiq/tools/scripts/run-broker.sh

# Build libp2p shared library (arm64)
nodeiq/tools/scripts/build-rust-android.sh

# Build Android app
nodeiq/tools/scripts/build-android.sh
```

Deploy the resulting APK to two devices. Configure one device as a provider (toggle feature flag or adjust `appsettings.json` agent). Launch the `ProviderService` via the in-app toggle (future UI) or via `adb shell am startservice`.

For Phase-2 libp2p testing, build and run the bootstrap service:

```bash
nodeiq/tools/scripts/build-bootstrap.sh
./target/release/nodeiq-bootstrap --listen /ip4/0.0.0.0/udp/8000/quic-v1 --namespace kmarket
```

Documentation lives under [`docs/`](docs/README.md) covering protocol and security details.
