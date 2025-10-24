#!/usr/bin/env bash
set -euo pipefail
ndk-build NDK_PROJECT_PATH=../android/app APP_BUILD_SCRIPT=../native/llama/CMakeLists.txt
