package ai.nodeiq.ui.state

import ai.nodeiq.model.Peer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class PeersViewModel : ViewModel() {
    private val _peers = MutableStateFlow(listOf<Peer>())
    val peers: StateFlow<List<Peer>> = _peers

    init {
        viewModelScope.launch {
            _peers.value = listOf(
                Peer(id = "ford_mech", displayName = "Ford Mechanic", skills = listOf("P03xx", "F150"), transport = "MQTT Secure")
            )
        }
    }
}
