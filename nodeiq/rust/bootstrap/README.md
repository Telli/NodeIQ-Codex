# NodeIQ Bootstrap Service

This crate implements a lightweight libp2p rendezvous and relay bootstrap node for local testing. It exposes QUIC on UDP/8000 and an HTTP health endpoint on port 8080. Build and run:

```bash
cargo run -- --listen /ip4/0.0.0.0/udp/8000/quic-v1 --health :8080 --namespace kmarket
```

A Dockerfile is provided to containerize the service for development environments.
