package ai.nodeiq.push

import ai.nodeiq.bg.ProviderServiceIntentBuilder
import android.util.Log
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

class FcmService : FirebaseMessagingService() {
    override fun onMessageReceived(message: RemoteMessage) {
        Log.i(TAG, "Received FCM message: ${message.data}")
        ProviderServiceIntentBuilder(applicationContext).start()
    }

    override fun onNewToken(token: String) {
        Log.i(TAG, "New FCM token $token")
    }

    companion object {
        private const val TAG = "NodeIQ:FcmService"
    }
}
