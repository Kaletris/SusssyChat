package hu.bme.aut.android.susssychat.usecases

import hu.bme.aut.android.susssychat.clients.ThreadsClient
import hu.bme.aut.android.susssychat.data.MessageResponse
import hu.bme.aut.android.susssychat.data.ThreadResponse
import java.io.IOException

class LoadThreadUseCase (private val client: ThreadsClient) {
    suspend operator fun invoke(accessToken: String, id: Int): Result<List<MessageResponse>> {
        return try {
            val messages = client.getMessages("Bearer $accessToken", id)
            Result.success(messages)
        } catch (e: IOException) {
            Result.failure(e)
        }
    }
}