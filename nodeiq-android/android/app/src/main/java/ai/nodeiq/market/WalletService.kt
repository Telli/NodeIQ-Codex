package ai.nodeiq.market

import ai.nodeiq.config.AppConfig
import ai.nodeiq.store.NodeIqDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext

class WalletService(
    private val db: NodeIqDatabase,
    @Suppress("unused") private val config: AppConfig
) {
    fun ledger(): Flow<List<ai.nodeiq.store.entities.LedgerEntity>> = db.ledgerDao().observeLedger()

    suspend fun balance(): Double = withContext(Dispatchers.IO) {
        db.ledgerDao().balance()
    }
}
