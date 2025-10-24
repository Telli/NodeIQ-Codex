package ai.nodeiq.config

import android.content.Context
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json

@Serializable
data class AppConfig(
    val feature_flags: FeatureFlags,
    val mqtt: MqttConfig,
    val topics: TopicConfig,
    val libp2p: Libp2pConfig,
    val ml: MlConfig,
    val marketplace: MarketplaceConfig,
    val agent: AgentConfig
) {
    companion object {
        fun load(context: Context): AppConfig {
            val bytes = context.assets.open("appsettings.json").use { it.readBytes() }
            return Json { ignoreUnknownKeys = true }.decodeFromString<AppConfig>(bytes.decodeToString())
        }
    }
}

@Serializable
data class FeatureFlags(val transport_mqtt: Boolean, val transport_libp2p: Boolean)

@Serializable
data class MqttConfig(
    val host: String,
    val port: Int,
    val tls_enabled: Boolean,
    val ca_cert_asset: String,
    val client_cert_asset: String,
    val client_key_asset: String,
    val keep_alive_sec: Int,
    val reconnect_backoff_sec: List<Int>
)

@Serializable
data class TopicConfig(val presence: String, val query: String, val response: String)

@Serializable
data class Libp2pConfig(
    val bootstrap_multiaddrs: List<String>,
    val namespace: String,
    val listen_on: List<String>,
    val enable_relay_client: Boolean
)

@Serializable
data class MlConfig(
    val embedding_model_asset: String,
    val llm_model_asset: String,
    val max_tokens: Int
)

@Serializable
data class MarketplaceConfig(
    val broker_api: String,
    val default_price_per_1k_tokens: Double,
    val min_query_price: Double
)

@Serializable
data class AgentConfig(
    val agent_id: String,
    val skills: List<String>
)
