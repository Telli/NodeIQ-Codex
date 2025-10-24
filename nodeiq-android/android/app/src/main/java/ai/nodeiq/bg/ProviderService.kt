package ai.nodeiq.bg

import ai.nodeiq.rag.RagEngine
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.IBinder
import androidx.core.app.NotificationCompat
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class ProviderService : Service() {

    @Inject lateinit var ragEngine: RagEngine

    override fun onCreate() {
        super.onCreate()
        createChannel()
        startForeground(FOREGROUND_ID, buildNotification("Listening for queries…"))
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        // TODO: hook transport listeners to ragEngine
        return START_STICKY
    }

    override fun onBind(intent: Intent?): IBinder? = null

    private fun createChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(CHANNEL_ID, "NodeIQ Provider", NotificationManager.IMPORTANCE_LOW)
            (getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager).createNotificationChannel(channel)
        }
    }

    private fun buildNotification(content: String): Notification = NotificationCompat.Builder(this, CHANNEL_ID)
        .setContentTitle("NodeIQ Provider")
        .setContentText(content)
        .setSmallIcon(android.R.drawable.ic_menu_info_details)
        .build()

    companion object {
        private const val CHANNEL_ID = "nodeiq_provider"
        private const val FOREGROUND_ID = 42
    }
}
