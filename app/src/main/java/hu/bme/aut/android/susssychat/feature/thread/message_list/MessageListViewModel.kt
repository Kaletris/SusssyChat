package hu.bme.aut.android.susssychat.feature.thread.message_list

import androidx.lifecycle.*
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import hu.bme.aut.android.susssychat.ChatApplication
import hu.bme.aut.android.susssychat.usecases.ChatUseCases
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch


class MessageListViewModel (
    private val threadOperations: ChatUseCases,
    private val savedStateHandle: SavedStateHandle,
    ) : ViewModel() {
        private val _state = MutableStateFlow(MessageListState())
        val state = _state.asStateFlow()

        init {
            loadMessagesList()
        }

        fun getToken(): String {
            return checkNotNull<String>(savedStateHandle["accessToken"])
        }

        fun getThreadId(): Int {
            return checkNotNull<Int>(savedStateHandle["id"])
        }

        fun onEvent(event: MessageListEvent) {
            when (event) {
                is MessageListEvent.CreateMessage -> {
                    viewModelScope.launch {
                        //threadOperations.createThreadUseCase()
                    }
                    loadMessagesList()

                }
                is MessageListEvent.DeleteMessage -> {
                    viewModelScope.launch {
                        //threadOperations.deleteThreadUseCase()
                    }
                    loadMessagesList()
                }
            }
        }

        private fun loadMessagesList() {
            val accessToken = getToken()
            viewModelScope.launch {
                _state.update { it.copy(isLoading = true) }
                try {
                    CoroutineScope(coroutineContext).launch(Dispatchers.IO) {
                        val messageList = threadOperations.loadThreadUseCase(accessToken, getThreadId()).getOrThrow()
                        _state.update {
                            it.copy(
                                isLoading = false,
                                messageList = messageList
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
                    MessageListViewModel(
                        threadOperations = threadOperations,
                        savedStateHandle = savedStateHandle,
                    )
                }
            }
        }
    }