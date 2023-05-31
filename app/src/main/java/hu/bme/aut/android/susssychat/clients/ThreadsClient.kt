package hu.bme.aut.android.susssychat.clients

import hu.bme.aut.android.susssychat.data.MessageResponse
import hu.bme.aut.android.susssychat.data.ThreadResponse
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Path

interface ThreadsClient {
    @GET("threads")
    suspend fun getThreads(@Header("Authorization") accessToken: String): List<ThreadResponse>

    @GET("threads/{threadId}/messages")
    suspend fun getMessages(
        @Header("Authorization") accessToken: String,
        @Path("threadId") threadId: Int
    ): List<MessageResponse>
}