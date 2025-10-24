#!/usr/bin/env bash
set -euo pipefail

CERT_DIR="$(cd "$(dirname "$0")"/../../broker/certs && pwd)"
mkdir -p "$CERT_DIR"

cd "$CERT_DIR"

if [ ! -f ca.key ]; then
  openssl genrsa -out ca.key 4096
  openssl req -x509 -new -nodes -key ca.key -sha256 -days 3650 -out ca.crt -subj "/CN=NodeIQ Dev CA"
fi

openssl genrsa -out broker.key 4096
openssl req -new -key broker.key -out broker.csr -subj "/CN=broker.nodeiq.local"
openssl x509 -req -in broker.csr -CA ca.crt -CAkey ca.key -CAcreateserial -out broker.crt -days 825 -sha256

openssl genrsa -out client.key 4096
openssl req -new -key client.key -out client.csr -subj "/CN=nodeiq-client"
openssl x509 -req -in client.csr -CA ca.crt -CAkey ca.key -CAcreateserial -out client.crt -days 825 -sha256
openssl pkcs8 -topk8 -nocrypt -inform PEM -in client.key -out client.key

echo "Certificates generated in $CERT_DIR"
