package hu.bme.aut.android.susssychat.feature.thread_list

import hu.bme.aut.android.susssychat.data.ThreadResponse
import hu.bme.aut.android.susssychat.ui.model.ThreadUi

data class ThreadListState (
    val isLoading: Boolean = false,
    val error: Throwable? = null,
    val isError: Boolean = error != null,
    val threadList: List<ThreadResponse> = emptyList()
)