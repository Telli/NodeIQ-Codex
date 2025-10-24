plugins {
    id("com.android.application") version "8.2.2" apply false
    id("org.jetbrains.kotlin.android") version "1.9.23" apply false
    id("org.jetbrains.kotlin.plugin.serialization") version "1.9.23" apply false
    id("com.google.dagger.hilt.android") version "2.49" apply false
    id("com.google.gms.google-services") version "4.4.1" apply false
    id("androidx.room") version "2.6.1" apply false
    id("com.google.protobuf") version "0.9.4" apply false
}

tasks.register("clean", Delete::class) {
    delete(rootProject.buildDir)
}
