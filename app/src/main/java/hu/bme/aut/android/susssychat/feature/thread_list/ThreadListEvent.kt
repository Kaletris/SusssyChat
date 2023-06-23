package hu.bme.aut.android.susssychat.feature.thread_list

import android.content.Context
import hu.bme.aut.android.susssychat.navigation.Screen

sealed class ThreadListEvent {
    data class CreateThread(val name: String) : ThreadListEvent()
    object DeleteThread : ThreadListEvent()
}