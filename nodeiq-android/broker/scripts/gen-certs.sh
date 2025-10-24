#!/usr/bin/env bash
set -euo pipefail
mkdir -p ../certs
openssl req -x509 -nodes -days 365 -newkey rsa:2048 -keyout ../certs/ca.key -out ../certs/ca.crt -subj "/CN=NodeIQ CA"
openssl req -nodes -new -newkey rsa:2048 -keyout ../certs/server.key -out server.csr -subj "/CN=broker.nodeiq.local"
openssl x509 -req -in server.csr -CA ../certs/ca.crt -CAkey ../certs/ca.key -CAcreateserial -out ../certs/server.crt -days 365
rm -f server.csr
