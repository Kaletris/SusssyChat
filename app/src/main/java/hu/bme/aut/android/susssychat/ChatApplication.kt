package hu.bme.aut.android.susssychat

import android.app.Application
import hu.bme.aut.android.susssychat.clients.ThreadsClient
import hu.bme.aut.android.susssychat.clients.TokenClient
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.security.SecureRandom
import java.security.cert.X509Certificate
import javax.net.ssl.SSLContext
import javax.net.ssl.SSLSession
import javax.net.ssl.TrustManager
import javax.net.ssl.X509TrustManager

class ChatApplication : Application(){
    companion object {
        lateinit var threadsClient: ThreadsClient
        lateinit var tokenClient: TokenClient
    }

    override fun onCreate() {
        super.onCreate()

        val retrofit = Retrofit.Builder()
            .baseUrl("https://10.0.2.2:7069")
            .addConverterFactory(GsonConverterFactory.create())
            .client(createTrustingOkHttpClient())
            .build()

        tokenClient = retrofit.create(TokenClient::class.java)
        threadsClient = retrofit.create(ThreadsClient::class.java)
    }

    fun createTrustingOkHttpClient(): OkHttpClient {
        return try {
            val x509TrustManager: X509TrustManager = object : X509TrustManager {
                override fun checkClientTrusted(
                    chain: Array<X509Certificate?>?,
                    authType: String?
                ) {
                }

                override fun checkServerTrusted(
                    chain: Array<X509Certificate?>?,
                    authType: String?
                ) {
                }

                override fun getAcceptedIssuers() = arrayOf<X509Certificate?>()
            }

            val trustAllCerts: Array<TrustManager> = arrayOf(
                x509TrustManager
            )

            val sslContext: SSLContext = SSLContext.getInstance("SSL")
            sslContext.init(null, trustAllCerts, SecureRandom())
            OkHttpClient.Builder()
                .sslSocketFactory(sslContext.getSocketFactory(), x509TrustManager)
                .hostnameVerifier { hostname: String?, session: SSLSession? -> true }
                .build()

        } catch (e: Exception) {
            throw RuntimeException(e)
        }
    }
}