package ru.yandex.praktikumchatapp.data

import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.retryWhen

class ChatRepository(
    private val api: ChatApi = ChatApi()
) {

    fun getReplyMessage(): Flow<String> {
        return api.getReply().retryWhen { cause, attempt ->
            delay(attempt * DELAY_TIME_MS)
            cause is Exception && attempt < MAX_RETRIES
        }
    }

    companion object {
        private const val DELAY_TIME_MS = 100
        private const val MAX_RETRIES = 10
    }
}