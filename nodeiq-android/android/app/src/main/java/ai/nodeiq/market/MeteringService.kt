package ai.nodeiq.market

import ai.nodeiq.config.AppConfig
import ai.nodeiq.util.DefaultJson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.Serializable
import kotlinx.serialization.encodeToString
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody

class MeteringService(
    @Suppress("unused") private val db: ai.nodeiq.store.NodeIqDatabase,
    private val config: AppConfig,
    private val client: OkHttpClient = OkHttpClient()
) {
    suspend fun record(event: MeteringEvent) = withContext(Dispatchers.IO) {
        val payload = DefaultJson.encodeToString(event)
        val request = Request.Builder()
            .url("${config.marketplace.broker_api}/metering")
            .post(payload.toRequestBody("application/json".toMediaType()))
            .build()
        client.newCall(request).execute().use {}
    }

    @Serializable
    data class MeteringEvent(
        val provider: String,
        val consumer: String,
        val queryId: String,
        val tokensOut: Int,
        val unitPrice: Double
    )
}
