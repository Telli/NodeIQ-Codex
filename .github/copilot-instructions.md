# Copilot Instructions for NodeIQ-Codex

## Repository Overview

NodeIQ-Codex is a monorepo containing the NodeIQ mobile application—a decentralized AI assistant enabling peer-to-peer knowledge exchange with on-device RAG and a credits marketplace. The project combines mobile (Android), systems (Rust), and infrastructure (MQTT) components.

## Repository Structure

```
NodeIQ-Codex/
├── docs/                    # Mobile UI blueprint and design documentation
│   └── ui/                  # Figma prompts, screens, flows, style guide, components
├── nodeiq/                  # Main monorepo with all implementation code
│   ├── android/             # Android application (Kotlin + Jetpack Compose)
│   ├── rust/                # Rust libp2p core, FFI, and bootstrap node
│   │   ├── core_p2p/        # libp2p implementation
│   │   ├── ffi/             # JNI bindings for Android
│   │   └── bootstrap/       # Rendezvous/relay server
│   ├── broker/              # Mosquitto MQTT broker configuration
│   ├── docs/                # Technical documentation (architecture, protocol)
│   └── tools/               # Build scripts and utilities
└── README.md                # Top-level overview
```

## Technology Stack

### Android Application
- **Language:** Kotlin (JVM target 17)
- **UI Framework:** Jetpack Compose with Material 3
- **Dependency Injection:** Hilt (Dagger)
- **Database:** Room
- **Messaging:** Firebase Cloud Messaging, MQTT (Eclipse Paho), Protobuf
- **Background Processing:** WorkManager, Foreground Services
- **Build System:** Gradle 8.4+ with Kotlin DSL
- **Testing:** JUnit, AndroidX Test

### Rust Components
- **Edition:** 2021
- **Key Libraries:** 
  - libp2p 0.53 (QUIC, Noise, identify, rendezvous, relay)
  - Tokio (async runtime)
  - Prost (Protobuf)
  - Serde (JSON serialization)
- **FFI:** C API compatible with JNI for Android integration
- **Build:** cargo, cargo-ndk for cross-compilation

### Infrastructure
- **MQTT Broker:** Mosquitto with TLS mutual authentication
- **Transport:** MQTT (primary), libp2p (feature-flagged for Phase 2)
- **Containerization:** Docker for broker deployment

## Architecture Patterns

### Android Application
- **Clean Architecture:** Separate layers for UI (Compose), domain logic, and data (Room, network)
- **MVVM:** ViewModels for UI state management
- **Repository Pattern:** Data layer abstraction for transport and persistence
- **Dependency Injection:** Use Hilt modules in `ai.nodeiq.di` package
- **Coroutines:** All async operations use Kotlin coroutines and Flow
- **Compose Navigation:** Single-activity architecture with composable screens

### Package Structure (Android)
- `ai.nodeiq.ui` - Composable screens and UI components
- `ai.nodeiq.transport` - Transport layer (MQTT, libp2p routing)
- `ai.nodeiq.store` - Room database entities, DAOs, converters
- `ai.nodeiq.model` - Domain models and data classes
- `ai.nodeiq.rag` - RAG engine (stubbed for MVP)
- `ai.nodeiq.bg` - Background services and workers
- `ai.nodeiq.di` - Hilt dependency injection modules
- `ai.nodeiq.telemetry` - Metrics and observability

### Rust Architecture
- **Modular Crates:** Separate core logic (`core_p2p`), FFI bindings (`ffi`), and binaries (`bootstrap`)
- **Async/Await:** Tokio runtime for all async operations
- **Error Handling:** Use `thiserror` for custom error types
- **Protocol:** libp2p request-response over QUIC with Noise encryption

## Development Workflow

### Building the Project

#### Android
```bash
# From nodeiq/android directory
./gradlew assembleDebug        # Build debug APK
./gradlew test                 # Run unit tests
./gradlew connectedAndroidTest # Run instrumented tests
```

Or use the helper script:
```bash
nodeiq/tools/scripts/build-android.sh
```

#### Rust
```bash
# Build core libp2p library
cd nodeiq/rust/core_p2p && cargo build --release

# Build JNI FFI library for Android
cd nodeiq/rust/ffi && cargo build --release

# Build bootstrap node
cd nodeiq/rust/bootstrap && cargo build --release

# Cross-compile for Android (requires cargo-ndk)
nodeiq/tools/scripts/build-rust-android.sh
```

#### MQTT Broker
```bash
# Generate TLS certificates
nodeiq/tools/scripts/gen-certs.sh

# Start Mosquitto broker
nodeiq/tools/scripts/run-broker.sh
```

### Testing Strategy

#### Android
- **Unit Tests:** Place in `src/test/` directories, use JUnit and MockK
- **Integration Tests:** Place in `src/androidTest/`, use AndroidX Test
- **Compose UI Tests:** Use Compose testing APIs with semantic matchers
- **Run tests before committing changes**

#### Rust
- **Unit Tests:** Inline with `#[cfg(test)]` modules
- **Integration Tests:** Place in `tests/` directory
- **Run:** `cargo test` in each crate directory

### Code Style

#### Kotlin
- Follow official Kotlin coding conventions
- Use ktlint for formatting (if configured)
- Prefer immutability (`val` over `var`)
- Use data classes for models
- Use sealed classes for state representations
- Prefer extension functions for utility methods
- Use trailing commas in multi-line declarations

#### Rust
- Follow standard Rust style guide
- Use `cargo fmt` for formatting
- Use `cargo clippy` for linting
- Prefer `Result` for error handling
- Use `async/await` for async code
- Document public APIs with doc comments (`///`)

## Protocol & Communication

### MQTT Topics
- Presence: `/kmarket/1.0.0/presence/{peer_id}`
- Queries: `/kmarket/1.0.0/query/{agent_id}`
- Responses: `/kmarket/1.0.0/response/{query_id}`

### Message Types
- QUERY: Request for knowledge with query_id, from_peer, query_text
- STREAM: Token delta responses with seq number
- END: Completion signal with status

See `nodeiq/docs/MQTT_TOPICS.md` and `nodeiq/docs/PROTOCOL.md` for details.

### libp2p (Phase 2)
- Uses Protobuf schema in `nodeiq/rust/core_p2p/proto/kmarket.proto`
- Request-response protocol over QUIC
- Noise encryption for security
- Rendezvous for peer discovery

## Common Tasks

### Adding a New Android Feature
1. Define domain models in `ai.nodeiq.model`
2. Create Room entities if persistence needed in `ai.nodeiq.store.entities`
3. Implement repository in appropriate package
4. Create ViewModel for UI state
5. Build Compose UI in `ai.nodeiq.ui`
6. Register dependencies in Hilt modules
7. Update navigation if needed
8. Add unit and UI tests

### Adding a New Rust Component
1. Define public API in lib.rs
2. Implement with async/await patterns
3. Add error types with thiserror
4. Write unit tests inline
5. Add integration tests if needed
6. Update FFI bindings if exposing to Android
7. Document public APIs

### Modifying Transport Layer
- Main router: `ai.nodeiq.transport.TransportRouter`
- MQTT implementation: `ai.nodeiq.transport.MQTTTransport`
- libp2p stub: `ai.nodeiq.transport.Lp2pTransport`
- Feature flags control which transport is active

## Dependencies

### Adding Android Dependencies
- Edit `nodeiq/android/app/build.gradle.kts`
- Use version catalogs if available
- Keep dependencies up to date with security patches
- Prefer AndroidX libraries over legacy support libraries

### Adding Rust Dependencies
- Edit appropriate `Cargo.toml` in `nodeiq/rust/*/`
- Specify versions explicitly
- Review transitive dependencies with `cargo tree`
- Keep libp2p and Tokio versions aligned across crates

## Documentation

- **Architecture:** `nodeiq/docs/ARCHITECTURE.md`
- **Protocol:** `nodeiq/docs/PROTOCOL.md`
- **MQTT Topics:** `nodeiq/docs/MQTT_TOPICS.md`
- **UI Design:** `docs/ui/` directory
- **README Files:** Check package-specific READMEs

## CI/CD

- **Android CI:** Builds on push to `nodeiq/android/**`, runs assembleDebug
- **Rust CI:** Builds on push to `nodeiq/rust/**`, compiles all crates
- Workflows defined in `nodeiq/.github/workflows/`

## Security Considerations

- Use TLS mutual authentication for MQTT
- Noise encryption for libp2p
- Never commit certificates or private keys
- Certificates generated via `tools/scripts/gen-certs.sh`
- Keep dependencies updated for security patches

## Key Concepts

- **Agent:** A knowledge provider with specific skills/expertise
- **Peer:** A node in the network (can be querier or provider)
- **Query:** Request for knowledge assistance
- **Stream:** Token-by-token response delivery
- **Credits:** Marketplace currency for knowledge exchange (future)
- **RAG:** Retrieval-Augmented Generation for on-device knowledge

## Getting Started for Contributors

1. Install prerequisites: JDK 17, Android Studio, Rust toolchain, Docker
2. Clone the repository
3. Generate TLS certificates: `nodeiq/tools/scripts/gen-certs.sh`
4. Start MQTT broker: `nodeiq/tools/scripts/run-broker.sh`
5. Build Rust library: `nodeiq/tools/scripts/build-rust-android.sh`
6. Open `nodeiq/android` in Android Studio
7. Build and run on emulator or device

## Best Practices

1. **Keep changes focused:** Make small, atomic commits
2. **Test thoroughly:** Write and run tests for new features
3. **Document public APIs:** Add KDoc for Kotlin, doc comments for Rust
4. **Follow conventions:** Match existing code style and patterns
5. **Review docs:** Update documentation when changing architecture or APIs
6. **Security first:** Never expose secrets, use TLS/encryption
7. **Mobile-first:** Optimize for mobile constraints (battery, network, storage)
8. **Feature flags:** Use configuration for experimental features (like libp2p)
