package ai.nodeiq.bg

import ai.nodeiq.R
import ai.nodeiq.rag.RagEngine
import ai.nodeiq.transport.P2PEvent
import ai.nodeiq.transport.TransportRouter
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
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@AndroidEntryPoint
class ProviderService : Service() {

    @Inject
    lateinit var transportRouter: TransportRouter

    private val scope = CoroutineScope(Dispatchers.IO)
    private var subscriptionJob: Job? = null

    override fun onCreate() {
        super.onCreate()
        createNotificationChannel()
        startForeground(NOTIFICATION_ID, buildNotification())
        scope.launch { transportRouter.start() }
        scope.launch {
            val agent = transportRouter.agentConfig
            transportRouter.advertise(agent.agentId, agent.skills)
        }
        subscriptionJob = scope.launch {
            transportRouter.events.collect { event ->
                when (event) {
                    is P2PEvent.QueryReceived -> handleQuery(event)
                    else -> Unit
                }
            }
        }
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        Timber.i("ProviderService started")
        return START_STICKY
    }

    override fun onDestroy() {
        subscriptionJob?.cancel()
        super.onDestroy()
    }

    override fun onBind(intent: Intent?): IBinder? = null

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val manager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            val channel = NotificationChannel(CHANNEL_ID, "NodeIQ Provider", NotificationManager.IMPORTANCE_LOW)
            manager.createNotificationChannel(channel)
        }
    }

    private fun buildNotification(): Notification =
        NotificationCompat.Builder(this, CHANNEL_ID)
            .setContentTitle(getString(R.string.provider_notification_title))
            .setContentText(getString(R.string.provider_notification_text))
            .setSmallIcon(android.R.drawable.stat_notify_sync)
            .setOngoing(true)
            .build()

    private fun handleQuery(event: P2PEvent.QueryReceived) {
        Timber.i("Handling query ${event.queryId} from ${event.fromPeer}")
        scope.launch {
            val stream = RagEngine.answerStream(event.queryText)
            transportRouter.respond(event.queryId, stream)
        }
    }

    companion object {
        private const val CHANNEL_ID = "nodeiq_provider"
        private const val NOTIFICATION_ID = 1001
    }
}
