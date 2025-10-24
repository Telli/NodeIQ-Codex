# NodeIQ MQTT Broker Certificates

Run `tools/scripts/gen-certs.sh` to generate a development certificate authority, broker certificate, and client certificates. The generated files will populate this directory:

- `ca.crt`
- `broker.crt`
- `broker.key`
- `client.crt`
- `client.key`

These certificates are referenced by the Mosquitto configuration and the Android application asset configuration.
