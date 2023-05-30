package hu.bme.aut.android.susssychat.feature.thread_list

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import hu.bme.aut.android.susssychat.usecases.ThreadUseCases

class ThreadListViewModel(
    private val threadOperations: ThreadUseCases
) : ViewModel() {

    private val _state = MutableStateFlow(ThreadListState())
    val state = _state.asStateFlow()

    init {
        loadThreadList()
    }

    fun onEvent(event: ThreadListEvent) {
        when (event) {
            is ThreadListEvent.CreateThread -> {
                viewModelScope.launch {
                    threadOperations.createThreadUseCase()
                }
                loadThreadList()

            }
            is ThreadListEvent.DeleteThread -> {
                viewModelScope.launch {
                    threadOperations.deleteThreadUseCase()
                }
                loadThreadList()
            }

        }
    }

    private fun loadThreadList() {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }
            try {
                CoroutineScope(coroutineContext).launch(Dispatchers.IO) {
                    val threadList = threadOperations.loadThreadListUseCase().getOrThrow().map { it.asTodoUi() }
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
                val threadOperations = ThreadListUseCases(TodoApplication.repository)
                ThreadListViewModel(
                    threadOperations = threadOperations
                )
            }
        }
    }
}