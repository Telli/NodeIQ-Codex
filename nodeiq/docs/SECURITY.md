# Security Posture

## MQTT

- TLS mutual authentication enforced by Mosquitto (`require_certificate true`).
- Client IDs follow the `nodeiq-{peer}` convention for ACL alignment.
- Presence announcements are retained with short broker TTL to limit stale state.
- Topic ACL enforces read/write separation between query and response channels.

## libp2p

- QUIC + Noise handshake ensures confidentiality and peer authentication.
- Persistent Ed25519 keys stored in Android's encrypted preferences (future work).
- Rendezvous namespace scoped to `kmarket` to avoid cross-project collisions.
- Circuit Relay v2 used as fallback to maintain connectivity through NATs.

## Mobile App

- WorkManager & Foreground service mitigate background execution limits for providers.
- Firebase Cloud Messaging optional wake-up path for high-priority requests.
- Sensitive configuration (certificates) stored as app assets during development; production should leverage Android Keystore and dynamic provisioning.

## Observability & Hardening

- Telemetry hook enables instrumentation for success rate, latency, and battery metrics.
- Scripts support certificate rotation for dev. Production should integrate with secure CA workflows.
