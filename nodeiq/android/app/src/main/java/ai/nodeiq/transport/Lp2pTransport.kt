package ai.nodeiq.transport

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch
import timber.log.Timber

class Lp2pTransport(
    private val scope: CoroutineScope
) : Transport {

    private val _events = MutableSharedFlow<P2PEvent>(extraBufferCapacity = 8, onBufferOverflow = BufferOverflow.DROP_OLDEST)
    override val events: SharedFlow<P2PEvent> = _events.asSharedFlow()
    private val started = MutableStateFlow(false)

    override suspend fun start() {
        if (started.value) return
        started.value = true
        Timber.i("libp2p transport is disabled by feature flag; emitting READY event for no-op mode")
        scope.launch { _events.emit(P2PEvent.Ready) }
    }

    override suspend fun advertise(agentId: String, skills: List<String>) {
        Timber.i("libp2p transport disabled; advertise skipped")
    }

    override suspend fun discover() {
        Timber.i("libp2p transport disabled; discover skipped")
    }

    override suspend fun sendQuery(agentId: String, queryText: String): Flow<ai.nodeiq.model.StreamPart> = flow {
        Timber.w("libp2p transport disabled; returning error stream")
        emit(ai.nodeiq.model.StreamPart.Error("libp2p transport disabled"))
    }

    override suspend fun respond(queryId: String, parts: Flow<ai.nodeiq.model.StreamPart>) {
        Timber.w("libp2p transport disabled; dropping provider stream for %s", queryId)
    }
}
