package ai.nodeiq.payments

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext

class PlayBillingPayments : Payments {
    override suspend fun topUp(amount: Double): Boolean = withContext(Dispatchers.IO) {
        delay(500)
        true
    }
}
