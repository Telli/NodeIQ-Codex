# NodeIQ Codex

NodeIQ Codex is a monorepo that contains the mobile-first NodeIQ decentralized knowledge network MVP.

The project is organized under the `nodeiq/` directory and includes:

- **Android application** with Kotlin, Jetpack Compose, Hilt, WorkManager, Firebase Cloud Messaging, MQTT transport, and feature-flagged libp2p integration hooks.
- **Rust libp2p core** compiled as a shared library for Android along with a bootstrap rendezvous+relay binary.
- **MQTT broker bundle** based on Mosquitto for local development with TLS mutual authentication.
- **Documentation** covering the architecture, protocol, security posture, MQTT topics, and libp2p integration notes.
- **Tooling scripts** for certificate generation, building Android and Rust artifacts, and running end-to-end tests.

The remainder of the repository resides under [`nodeiq/`](nodeiq/README.md) with detailed setup instructions for developers.
