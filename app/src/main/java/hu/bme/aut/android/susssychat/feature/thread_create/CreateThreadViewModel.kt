package hu.bme.aut.android.susssychat.feature.thread_create

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import hu.bme.aut.android.susssychat.ui.model.toUiText
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class CreateThreadViewModel (
    private val todoOperations: TodoUseCases
    ) : ViewModel() {

        private val _state = MutableStateFlow(CreateTodoState())
        val state = _state.asStateFlow()

        private val _uiEvent = Channel<UiEvent>()
        val uiEvent = _uiEvent.receiveAsFlow()

        fun onEvent(event: CreateTodoEvent) {
            when(event) {
                is CreateTodoEvent.ChangeTitle -> {
                    val newValue = event.text
                    _state.update { it.copy(
                        todo = it.todo.copy(title = newValue)
                    ) }
                }
                is CreateTodoEvent.ChangeDescription -> {
                    val newValue = event.text
                    _state.update { it.copy(
                        todo = it.todo.copy(description = newValue)
                    ) }
                }
                is CreateTodoEvent.SelectPriority -> {
                    val newValue = event.priority
                    _state.update { it.copy(
                        todo = it.todo.copy(priority = newValue)
                    ) }
                }
                is CreateTodoEvent.SelectDate -> {
                    val newValue = event.date
                    _state.update { it.copy(
                        todo = it.todo.copy(dueDate = newValue.toString())
                    ) }
                }
                CreateTodoEvent.SaveTodo -> {
                    onSave()
                }
            }
        }

        private fun onSave() {
            viewModelScope.launch {
                try {
                    todoOperations.saveTodo(state.value.todo.asTodo())
                    _uiEvent.send(UiEvent.Success)
                } catch (e: Exception) {
                    _uiEvent.send(UiEvent.Failure(e.toUiText()))
                }
            }
        }

        companion object {
            val Factory: ViewModelProvider.Factory = viewModelFactory {
                initializer {
                    val todoOperations = TodoUseCases(TodoApplication.repository)
                    CreateTodoViewModel(
                        todoOperations = todoOperations
                    )
                }
            }
        }


    }