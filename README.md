# NodeIQ Android Monorepo
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

This repository bootstraps the NodeIQ Android client alongside native modules, Rust transports, and marketplace services required for on-device retrieval augmented generation.
