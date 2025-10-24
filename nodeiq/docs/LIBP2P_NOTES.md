# libp2p Phase-2 Notes

The Android app ships with a feature-flagged libp2p transport. To enable:

1. Update `appsettings.json` to set `"transport_libp2p": true` and `"transport_mqtt": false`.
2. Build the Rust FFI shared library via `tools/scripts/build-rust-android.sh` and ensure `libnodeiq_p2p.so` is refreshed.
3. Run the bootstrap service (`nodeiq-bootstrap`) and expose UDP/8000 + HTTP/8080.
4. Provide a rendezvous namespace JSON to `niq_advertise` / `niq_discover` when wiring JNI (placeholder functions in this MVP).

The JNI surface is intentionally minimal and can be extended to map libp2p events to the Android `Transport` interface. Noise + QUIC is the default transport stack with request-response semantics around the `/kmarket/1.0.0` protocol defined in protobuf.
