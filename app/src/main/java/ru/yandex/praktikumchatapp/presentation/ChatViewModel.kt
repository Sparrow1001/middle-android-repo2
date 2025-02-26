package ru.yandex.praktikumchatapp.presentation

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import ru.yandex.praktikumchatapp.data.ChatRepository

class ChatViewModel(
    val isWithReplies: Boolean = true
) : ViewModel() {

    private val repository = ChatRepository()

    private val _messages = MutableStateFlow(emptyList<Message>())
    val messages = _messages.asStateFlow()

    init {
        viewModelScope.launch {
            while (isWithReplies) {
                try {
                    repository.getReplyMessage().collect { response ->
                        _messages.update {
                            it + Message.OtherMessage(response)
                        }
                    }
                } catch (e: Exception) {
                    Log.d(TAG, e.message.toString())
                }
            }
        }
    }

    fun sendMyMessage(messageText: String) {
        _messages.update {
            it + Message.MyMessage(messageText)
        }
    }

    companion object {
        private const val TAG = "ChatViewModel"
    }
}