package ai.nodeiq.market

import ai.nodeiq.config.AppConfig
import ai.nodeiq.util.DefaultJson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.encodeToString
import kotlinx.serialization.Serializable
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.MediaType.Companion.toMediaType

class PricingService(
    @Suppress("unused") private val db: ai.nodeiq.store.NodeIqDatabase,
    private val config: AppConfig,
    private val client: OkHttpClient = OkHttpClient()
) {
    suspend fun updatePrice(pricePer1k: Double, minQueryPrice: Double) = withContext(Dispatchers.IO) {
        val payload = DefaultJson.encodeToString(PriceUpdate(pricePer1k, minQueryPrice))
        val request = Request.Builder()
            .url("${config.marketplace.broker_api}/pricing/${config.agent.agent_id}")
            .post(payload.toRequestBody("application/json".toMediaType()))
            .build()
        client.newCall(request).execute().use {}
    }

    @Serializable
    data class PriceUpdate(val pricePer1kTokens: Double, val minQueryPrice: Double)
}
