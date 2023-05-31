package hu.bme.aut.android.susssychat.usecases

import hu.bme.aut.android.susssychat.clients.ThreadsClient
import hu.bme.aut.android.susssychat.data.ThreadResponse
import java.io.IOException

class LoadThreadListUseCase(private val client: ThreadsClient) {
        suspend operator fun invoke(accessToken: String): Result<List<ThreadResponse>> {
            return try {
                val threads = client.getThreads("Bearer $accessToken")
                Result.success(threads)
            } catch (e: IOException) {
                Result.failure(e)
            }
        }
}