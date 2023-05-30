package hu.bme.aut.android.susssychat.usecases

import java.io.IOException

class LoadThreadListUseCase (private val repository: TodoRepository) {

        suspend operator fun invoke(): Result<List<Todo>> {
            return try {
                val todos = repository.getAllTodos().first()
                Result.success(todos.map { it.asTodo() })
            } catch (e: IOException) {
                Result.failure(e)
            }
        }
}