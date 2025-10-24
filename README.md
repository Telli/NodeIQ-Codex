# NodeIQ-Codex

Design documentation for the NodeIQ mobile application — a decentralized AI assistant enabling peer-to-peer knowledge exchange with on-device RAG and a credits marketplace.

## Contents
- `docs/` – Mobile UI blueprint, including style guide, screen specs, component library, navigation flows, and Figma prompt.

## Overview
The NodeIQ Android experience combines MQTT-secured messaging with optional P2P transport, allowing users to:
- Discover nearby or global peers and request knowledge assistance.
- Operate their own agent in provider mode, pricing access per 1k tokens.
- Manage credits through a built-in wallet and track marketplace earnings.
- Maintain a private, on-device knowledge base for retrieval-augmented responses.

All documents follow Material 3 conventions and outline accessibility considerations, micro-interactions, and engineering notes for Jetpack Compose implementation.
# NodeIQ Codex

NodeIQ Codex is a monorepo that contains the mobile-first NodeIQ decentralized knowledge network MVP.

The project is organized under the `nodeiq/` directory and includes:

- **Android application** with Kotlin, Jetpack Compose, Hilt, WorkManager, Firebase Cloud Messaging, MQTT transport, and feature-flagged libp2p integration hooks.
- **Rust libp2p core** compiled as a shared library for Android along with a bootstrap rendezvous+relay binary.
- **MQTT broker bundle** based on Mosquitto for local development with TLS mutual authentication.
- **Documentation** covering the architecture, protocol, security posture, MQTT topics, and libp2p integration notes.
- **Tooling scripts** for certificate generation, building Android and Rust artifacts, and running end-to-end tests.

The remainder of the repository resides under [`nodeiq/`](nodeiq/README.md) with detailed setup instructions for developers.
