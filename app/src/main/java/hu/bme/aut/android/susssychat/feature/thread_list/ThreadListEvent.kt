package hu.bme.aut.android.susssychat.feature.thread_list

import android.content.Context
import hu.bme.aut.android.susssychat.navigation.Screen

sealed class ThreadListEvent {
    object CreateThread : ThreadListEvent()
    object DeleteThread : ThreadListEvent()
}