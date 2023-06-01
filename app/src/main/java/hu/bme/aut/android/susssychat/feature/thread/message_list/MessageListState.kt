package hu.bme.aut.android.susssychat.feature.thread.message_list

import hu.bme.aut.android.susssychat.data.MessageResponse
import hu.bme.aut.android.susssychat.data.ThreadResponse

data class MessageListState (
    val isLoading: Boolean = false,
    val error: Throwable? = null,
    val isError: Boolean = error != null,
    val messageList: List<MessageResponse> = emptyList()
)