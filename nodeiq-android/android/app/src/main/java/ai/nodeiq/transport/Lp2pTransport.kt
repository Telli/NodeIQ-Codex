package ai.nodeiq.transport

import ai.nodeiq.config.AppConfig

class Lp2pTransport(
    private val config: AppConfig
) : Transport {
    override val name: String = "libp2p"
    override val enabled: Boolean = config.feature_flags.transport_libp2p

    init {
        if (enabled) {
            runCatching { System.loadLibrary("nodeiq_p2p") }
        }
    }

    override suspend fun start() {
        // TODO bridge to JNI start
    }

    override suspend fun stop() {
        // TODO bridge to JNI stop
    }
}
