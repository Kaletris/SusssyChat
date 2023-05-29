package hu.bme.aut.android.susssychat

import android.net.Uri
import android.net.http.SslError
import android.os.Bundle
import android.util.Log
import android.view.ViewGroup
import android.webkit.SslErrorHandler
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView
import hu.bme.aut.android.susssychat.clients.TokenClient
import hu.bme.aut.android.susssychat.ui.theme.SusssyChatTheme
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.security.SecureRandom
import java.security.cert.X509Certificate
import javax.net.ssl.SSLContext
import javax.net.ssl.SSLSession
import javax.net.ssl.TrustManager
import javax.net.ssl.X509TrustManager


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val retrofit = Retrofit.Builder()
            .baseUrl("https://10.0.2.2:7069")
            .addConverterFactory(GsonConverterFactory.create())
            .client(createTrustingOkHttpClient())
            .build()

        val tokenClient = retrofit.create(TokenClient::class.java)

        setContent {
            SusssyChatTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    Login(tokenClient)
                }
            }
        }
    }

    private fun createTrustingOkHttpClient(): OkHttpClient {
        return try {
            val x509TrustManager: X509TrustManager = object : X509TrustManager {
                override fun checkClientTrusted(chain: Array<X509Certificate?>?, authType: String?) {}
                override fun checkServerTrusted(chain: Array<X509Certificate?>?, authType: String?) {}
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

@Composable
fun Login(tokenClient: TokenClient) {
    AndroidView(factory = {
        WebView(it).apply {
            ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
            )
            webViewClient = object : WebViewClient () {
                override fun onReceivedSslError(
                    view: WebView?,
                    handler: SslErrorHandler?,
                    error: SslError?
                ) {
                    handler?.proceed()
                }

                override fun shouldOverrideUrlLoading(
                    view: WebView?,
                    request: WebResourceRequest?
                ): Boolean {
                    request?.let { req ->
                        if (req.url.toString().startsWith("https://localhost:5002/signin-oidc")) {
                            val authCode = request.url.getQueryParameter("code")!!

                            GlobalScope.launch {
                                val response = tokenClient.getToken(
                                    "client",
                                    "authorization_code",
                                    authCode,
                                    "https://localhost:5002/signin-oidc"
                                )

                                Log.d("alma", response.accessToken)
                            }
                        }
                    }

                    return super.shouldOverrideUrlLoading(view, request)
                }
            }

            val loginUrl = Uri.parse("https://10.0.2.2:7069/connect/authorize")
                .buildUpon()
                .appendQueryParameter("client_id", "client")
                .appendQueryParameter("redirect_uri", "https://localhost:5002/signin-oidc")
                .appendQueryParameter("response_type", "code")
                .appendQueryParameter("scope", "openid offline_access")
                .build()

            Log.d("anyád", loginUrl.toString())

            loadUrl(loginUrl.toString())
        }
    })
}