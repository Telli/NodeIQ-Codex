package ai.nodeiq.payments

interface Payments {
    suspend fun topUp(amount: Double): Boolean
}
