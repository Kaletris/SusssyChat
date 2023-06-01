package hu.bme.aut.android.susssychat.feature.thread_list

import androidx.lifecycle.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import hu.bme.aut.android.susssychat.ChatApplication
import hu.bme.aut.android.susssychat.usecases.ChatUseCases

class ThreadListViewModel(
    private val threadOperations: ChatUseCases,
    private val savedStateHandle: SavedStateHandle,
) : ViewModel() {

    private val _state = MutableStateFlow(ThreadListState())
    val state = _state.asStateFlow()

    init {
        loadThreadList()
    }

    fun getToken(): String {
        return checkNotNull<String>(savedStateHandle["accessToken"])
    }

    fun onEvent(event: ThreadListEvent) {
        when (event) {
            is ThreadListEvent.CreateThread -> {
                viewModelScope.launch {
                    //threadOperations.createThreadUseCase()
                }
                loadThreadList()

            }
            is ThreadListEvent.DeleteThread -> {
                viewModelScope.launch {
                    //threadOperations.deleteThreadUseCase()
                }
                loadThreadList()
            }

        }
    }

    private fun loadThreadList() {
        val accessToken = getToken()
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }
            try {
                CoroutineScope(coroutineContext).launch(Dispatchers.IO) {
                    val threadList = threadOperations.loadThreadListUseCase(accessToken).getOrThrow()
                    _state.update {
                        it.copy(
                            isLoading = false,
                            threadList = threadList
                        )
                    }
                }
            } catch (e: Exception) {
                _state.update {
                    it.copy(
                        isLoading = false,
                        error = e
                    )
                }
            }
        }
    }

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val savedStateHandle = createSavedStateHandle()
                val threadOperations = ChatUseCases(ChatApplication.threadsClient)
                ThreadListViewModel(
                    threadOperations = threadOperations,
                    savedStateHandle = savedStateHandle,
                )
            }
        }
    }
}