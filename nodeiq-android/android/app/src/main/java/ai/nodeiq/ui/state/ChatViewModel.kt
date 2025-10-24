package ai.nodeiq.ui.state

import ai.nodeiq.model.Message
import ai.nodeiq.rag.RagEngine
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch

@HiltViewModel
class ChatViewModel @Inject constructor(
    private val ragEngine: RagEngine
) : ViewModel() {

    private val _streaming = MutableStateFlow(false)
    val streaming: StateFlow<Boolean> = _streaming.asStateFlow()

    fun observeConversation(peerId: String) = flow {
        emit(listOf(Message(role = "user", text = "Describe issue for $peerId")))
        streamQuery("Describe issue for $peerId")
    }

    private fun streamQuery(prompt: String) {
        viewModelScope.launch {
            _streaming.value = true
            ragEngine.answerStream(prompt).collect {
                if (it.isTerminal) {
                    _streaming.value = false
                }
            }
        }
    }
}
