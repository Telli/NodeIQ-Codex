package ai.nodeiq.ui

import ai.nodeiq.model.StreamPart
import ai.nodeiq.transport.TransportRouter
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
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
fun ChatScreen(agentId: String, viewModel: ChatViewModel = hiltViewModel()) {
    LaunchedEffect(agentId) {
        viewModel.bind(agentId)
    }
    val uiState by viewModel.uiState.collectAsState()
    val inputState = remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(text = "Transport: ${uiState.transportLabel}", style = MaterialTheme.typography.bodyMedium)
        LazyColumn(modifier = Modifier.weight(1f)) {
            itemsIndexed(uiState.messages) { _, message ->
                Text(text = message, style = MaterialTheme.typography.bodyLarge)
            }
        }
        OutlinedTextField(
            value = inputState.value,
            onValueChange = { inputState.value = it },
            modifier = Modifier.fillMaxWidth(),
            label = { Text(text = "Query") }
        )
        Button(onClick = {
            viewModel.sendQuery(inputState.value)
            inputState.value = ""
        }, enabled = inputState.value.isNotBlank(), modifier = Modifier.fillMaxWidth()) {
            Text(text = "Send")
        }
    }
}

data class ChatUiState(
    val agentId: String = "",
    val transportLabel: String = "MQTT (TLS)",
    val messages: List<String> = emptyList()
)

@HiltViewModel
class ChatViewModel @Inject constructor(
    private val transportRouter: TransportRouter
) : ViewModel() {
    private val _uiState = MutableStateFlow(ChatUiState())
    val uiState: StateFlow<ChatUiState> = _uiState.asStateFlow()

    fun bind(agentId: String) {
        val label = if (transportRouter.agentConfig.agentId == agentId && transportRouter.agentConfig.skills.isNotEmpty()) {
            "MQTT (TLS)"
        } else {
            if (transportRouter.agentConfig.agentId.isNotEmpty()) "MQTT (TLS)" else "libp2p (QUIC)"
        }
        _uiState.value = ChatUiState(agentId = agentId, transportLabel = label)
    }

    fun sendQuery(text: String) {
        val agentId = _uiState.value.agentId
        viewModelScope.launch {
            transportRouter.sendQuery(agentId, text).collect { part ->
                when (part) {
                    is StreamPart.Delta -> appendMessage(part.delta)
                    is StreamPart.End -> appendMessage("[${part.status}]")
                    is StreamPart.Error -> appendMessage("Error: ${part.message}")
                }
            }
        }
    }

    private fun appendMessage(message: String) {
        val updated = _uiState.value.messages + message
        _uiState.value = _uiState.value.copy(messages = updated)
    }
}
