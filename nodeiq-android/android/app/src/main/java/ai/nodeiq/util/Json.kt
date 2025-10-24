package ai.nodeiq.util

import kotlinx.serialization.json.Json

val DefaultJson = Json {
    ignoreUnknownKeys = true
    encodeDefaults = true
    prettyPrint = true
}
