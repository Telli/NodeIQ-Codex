# Security Model

- Mutual TLS for MQTT ensures peer authentication.
- All knowledge base data remains on-device.
- libp2p transport uses Noise handshakes over QUIC with feature flag gating.
- Provider service runs as a foreground service to maintain uptime and visibility.
