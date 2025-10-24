package ai.nodeiq.telemetry

import android.util.Log

object Telemetry {
    fun track(event: String, properties: Map<String, Any?> = emptyMap()) {
        Log.d("NodeIQ:Telemetry", "event=$event props=$properties")
    }
}
