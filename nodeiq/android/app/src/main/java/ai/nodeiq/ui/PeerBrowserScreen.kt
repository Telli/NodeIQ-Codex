package ai.nodeiq.ui

import ai.nodeiq.model.PeerSummary
import ai.nodeiq.transport.P2PEvent
import ai.nodeiq.transport.TransportRouter
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@Composable
fun PeerBrowserScreen(onPeerSelected: (String) -> Unit, viewModel: PeerBrowserViewModel = hiltViewModel()) {
    val peers by viewModel.peers.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.start()
    }

    Column(modifier = Modifier.padding(16.dp)) {
        peers.forEach { peer ->
            PeerCard(peer, onPeerSelected)
        }
    }
}

@Composable
private fun PeerCard(peer: PeerSummary, onPeerSelected: (String) -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
            .clickable { onPeerSelected(peer.agentId) }
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = peer.agentId, style = MaterialTheme.typography.titleMedium)
            Text(text = peer.skills.joinToString(), style = MaterialTheme.typography.bodyMedium)
        }
    }
}

@HiltViewModel
class PeerBrowserViewModel @Inject constructor(
    private val transportRouter: TransportRouter
) : ViewModel() {

    private val _peers = MutableStateFlow<List<PeerSummary>>(emptyList())
    val peers: StateFlow<List<PeerSummary>> = _peers.asStateFlow()
    private var started = false

    fun start() {
        if (started) return
        started = true
        viewModelScope.launch { transportRouter.start() }
        viewModelScope.launch { transportRouter.discover() }
        viewModelScope.launch {
            transportRouter.events.collect { event ->
                when (event) {
                    is P2PEvent.PeerDiscovered -> addPeer(event)
                    else -> Unit
                }
            }
        }
    }

    private fun addPeer(event: P2PEvent.PeerDiscovered) {
        val updated = _peers.value.toMutableList()
        val existingIndex = updated.indexOfFirst { it.agentId == event.agentId }
        val summary = PeerSummary(event.peerId, event.agentId, event.skills)
        if (existingIndex >= 0) {
            updated[existingIndex] = summary
        } else {
            updated.add(summary)
        }
        _peers.value = updated.sortedBy { it.agentId }
    }
}
