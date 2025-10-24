package ai.nodeiq.transport

import ai.nodeiq.config.AppConfig

interface Transport {
    val name: String
    val enabled: Boolean
    suspend fun start()
    suspend fun stop()
}

class TransportRegistry(private val transports: List<Transport>) {
    fun activeTransports(): List<Transport> = transports.filter { it.enabled }

    companion object {
        fun fromConfig(config: AppConfig, transports: List<Transport>): TransportRegistry =
            TransportRegistry(transports.filter { transport ->
                when (transport.name) {
                    "MQTT" -> config.feature_flags.transport_mqtt
                    "libp2p" -> config.feature_flags.transport_libp2p
                    else -> false
                }
            })
    }
}
