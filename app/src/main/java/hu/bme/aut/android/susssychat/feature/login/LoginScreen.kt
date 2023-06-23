package hu.bme.aut.android.susssychat.feature.login

import android.net.Uri
import android.net.http.SslError
import android.util.Log
import android.view.ViewGroup
import android.webkit.SslErrorHandler
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.ui.viewinterop.AndroidView
import hu.bme.aut.android.susssychat.ChatApplication
import hu.bme.aut.android.susssychat.clients.TokenClient
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

@OptIn(ExperimentalFoundationApi::class)
@ExperimentalMaterial3Api
@Composable
fun LoginScreen(onLogin: (accessToken: String) -> Unit) {
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
                                val response = ChatApplication.tokenClient.getToken(
                                    "client",
                                    "authorization_code",
                                    authCode,
                                    "https://localhost:5002/signin-oidc"
                                )

                                launch(Dispatchers.Main) {
                                    onLogin(response.accessToken)
                                }
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

            loadUrl(loginUrl.toString())
        }
    })
}
