# Android NDK Toolchain Setup

The NodeIQ Android build requires the Android NDK for compiling the Rust libp2p core to a shared object. Install the NDK using the Android SDK manager and ensure the `ANDROID_NDK_HOME` environment variable is set. The helper script `tools/scripts/build-rust-android.sh` will detect the NDK via the SDK manager or the environment variable.
