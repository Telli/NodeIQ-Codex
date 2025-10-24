#!/usr/bin/env bash
set -euo pipefail

ROOT_DIR="$(cd "$(dirname "$0")"/../.. && pwd)"
LOG_DIR="$ROOT_DIR/.e2e-logs"
mkdir -p "$LOG_DIR"

echo "Starting Mosquitto broker"
(pushd "$ROOT_DIR/broker" >/dev/null && docker compose up -d)

trap 'echo "Stopping broker"; (cd "$ROOT_DIR/broker" && docker compose down)' EXIT

echo "Placeholder for Android instrumentation and device orchestration"
echo "Collect logs with adb logcat > $LOG_DIR/device.log"
