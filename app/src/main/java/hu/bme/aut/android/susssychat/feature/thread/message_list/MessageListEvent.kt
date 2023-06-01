package hu.bme.aut.android.susssychat.feature.thread.message_list

import hu.bme.aut.android.susssychat.feature.thread_list.ThreadListEvent

sealed class MessageListEvent {
    object CreateMessage : MessageListEvent()
    object DeleteMessage : MessageListEvent()
}