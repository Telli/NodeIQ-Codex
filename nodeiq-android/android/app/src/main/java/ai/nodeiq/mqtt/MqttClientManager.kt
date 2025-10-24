package ai.nodeiq.mqtt

import android.content.Context
import org.eclipse.paho.client.mqttv3.IMqttClient
import org.eclipse.paho.client.mqttv3.MqttClient
import org.eclipse.paho.client.mqttv3.MqttConnectOptions
import java.util.UUID

class MqttClientManager(private val context: Context) {
    fun createClient(serverUri: String): IMqttClient {
        val clientId = "nodeiq-${UUID.randomUUID()}"
        val client = MqttClient(serverUri, clientId)
        val options = MqttConnectOptions().apply {
            isCleanSession = false
            keepAliveInterval = 30
        }
        client.connect(options)
        return client
    }
}
