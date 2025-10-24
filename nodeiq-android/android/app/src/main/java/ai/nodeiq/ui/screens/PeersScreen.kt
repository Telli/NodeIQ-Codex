package ai.nodeiq.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import ai.nodeiq.ui.state.PeersViewModel

@Composable
fun PeersScreen(
    modifier: Modifier = Modifier,
    onPeerSelected: (String) -> Unit = {}
) {
    val vm: PeersViewModel = viewModel()
    val peers by vm.peers.collectAsState()

    Column(modifier = modifier.fillMaxSize().padding(16.dp)) {
        peers.forEach { peer ->
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp)
                    .clickable { onPeerSelected(peer.id) }
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(peer.displayName, style = MaterialTheme.typography.titleMedium)
                    Text(peer.skills.joinToString(), style = MaterialTheme.typography.bodySmall)
                }
            }
        }
    }
}
