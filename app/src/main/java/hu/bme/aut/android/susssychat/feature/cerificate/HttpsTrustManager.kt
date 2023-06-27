package hu.bme.aut.android.susssychat.feature.cerificate

import hu.bme.aut.android.susssychat.feature.cerificate.HttpsTrustManager.Companion.trustManagers
import java.security.KeyManagementException
import java.security.NoSuchAlgorithmException
import java.security.SecureRandom
import javax.net.ssl.*
import java.security.cert.X509Certificate

class HttpsTrustManager : X509TrustManager {

    override fun checkClientTrusted(x509Certificates: Array<X509Certificate>, s: String) {
        // Do nothing (accept all clients)
    }

    override fun checkServerTrusted(x509Certificates: Array<X509Certificate>, s: String) {
        // Do nothing (accept all servers)
    }

    fun isClientTrusted(chain: Array<X509Certificate>): Boolean {
        return true
    }

    fun isServerTrusted(chain: Array<X509Certificate>): Boolean {
        return true
    }

    override fun getAcceptedIssuers(): Array<X509Certificate> {
        return _AcceptedIssuers
    }

    companion object {
        private var trustManagers: Array<TrustManager>? = null
        private val _AcceptedIssuers = arrayOf<X509Certificate>()

        fun allowAllSSL() {
            HttpsURLConnection.setDefaultHostnameVerifier { _, _ -> true }

            var context: SSLContext? = null
            if (trustManagers == null) {
                trustManagers = arrayOf(HttpsTrustManager())
            }

            try {
                context = SSLContext.getInstance("TLS")
                context.init(null, trustManagers, SecureRandom())
            } catch (e: NoSuchAlgorithmException) {
                e.printStackTrace()
            } catch (e: KeyManagementException) {
                e.printStackTrace()
            }

            HttpsURLConnection.setDefaultSSLSocketFactory(context?.socketFactory)
        }
    }
}