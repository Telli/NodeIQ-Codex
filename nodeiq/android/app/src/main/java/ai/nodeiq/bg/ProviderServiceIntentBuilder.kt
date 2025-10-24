package ai.nodeiq.bg

import android.content.Context
import android.content.Intent
import android.os.Build

class ProviderServiceIntentBuilder(private val context: Context) {
    fun build(): Intent = Intent(context, ProviderService::class.java)

    fun start() {
        val intent = build()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            context.startForegroundService(intent)
        } else {
            context.startService(intent)
        }
    }
}
