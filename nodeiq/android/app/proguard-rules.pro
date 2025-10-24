# Placeholder ProGuard configuration for NodeIQ.
# Keep Kotlin metadata and Hilt generated classes.
-keep class kotlinx.serialization.** { *; }
-keep class dagger.hilt.** { *; }
-keep class ai.nodeiq.** { *; }
