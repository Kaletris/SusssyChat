package hu.bme.aut.android.susssychat.clients

import hu.bme.aut.android.susssychat.data.TokenResponse
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface TokenClient {
    @FormUrlEncoded
    @POST("connect/token")
    suspend fun getToken(
        @Field("client_id")
        clientId: String,

        @Field("grant_type")
        grantType: String,

        @Field("code")
        code: String,

        @Field("redirect_uri")
        redirectUri: String
    ): TokenResponse
}