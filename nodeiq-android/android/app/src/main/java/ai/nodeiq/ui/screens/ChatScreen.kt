package ai.nodeiq.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import ai.nodeiq.ui.state.ChatViewModel

@Composable
fun ChatScreen(peerId: String, viewModel: ChatViewModel = hiltViewModel()) {
    val conversationFlow = remember(peerId) { viewModel.observeConversation(peerId) }
    val state by conversationFlow.collectAsState(initial = emptyList())
    val streaming by viewModel.streaming.collectAsState()

    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        LazyColumn(modifier = Modifier.weight(1f)) {
            items(state) { message ->
                Text(text = "${message.role}: ${message.text}")
            }
        }
        if (streaming) {
            Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.fillMaxSize()) {
                CircularProgressIndicator()
                Text("Streaming response…")
            }
        }
    }
}
