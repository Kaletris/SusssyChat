package hu.bme.aut.android.susssychat.feature.thread_list

import androidx.lifecycle.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.microsoft.signalr.HubConnection
import com.microsoft.signalr.HubConnectionBuilder
import hu.bme.aut.android.susssychat.ChatApplication
import hu.bme.aut.android.susssychat.feature.cerificate.HttpsTrustManager
import hu.bme.aut.android.susssychat.usecases.ChatUseCases
import io.reactivex.rxjava3.core.Single
import java.security.SecureRandom
import java.security.cert.X509Certificate
import javax.net.ssl.SSLContext
import javax.net.ssl.SSLSession
import javax.net.ssl.TrustManager
import javax.net.ssl.X509TrustManager

class ThreadListViewModel(
    private val threadOperations: ChatUseCases,
    private val savedStateHandle: SavedStateHandle,
) : ViewModel() {

    private val _state = MutableStateFlow(ThreadListState())
    val state = _state.asStateFlow()

    init {
        //HttpsTrustManager.allowAllSSL()

        val hubConnection: HubConnection = HubConnectionBuilder.create("https://10.0.2.2:7069/notifications")
            .setHttpClientBuilderCallback {
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

                it.sslSocketFactory(sslContext.getSocketFactory(), x509TrustManager)
                .hostnameVerifier { hostname: String?, session: SSLSession? -> true }
            }


            //.withAccessTokenProvider(Single.just(getToken()))
            .build()

        hubConnection.on(
            "ThreadCreated"
        ) {
            loadThreadList()
        }

        hubConnection.on(
            "ThreadDeleted"
        ) {
            loadThreadList()
        }

        viewModelScope.launch (Dispatchers.IO) {
            hubConnection.start().blockingAwait()
        }


        loadThreadList()

    }

    fun getToken(): String {
        return checkNotNull<String>(savedStateHandle["accessToken"])
    }

    fun onEvent(event: ThreadListEvent) {
        when (event) {
            is ThreadListEvent.CreateThread -> {
                viewModelScope.launch {
                    //threadOperations.createThreadUseCase(event.name)
                }
                loadThreadList()

            }
            is ThreadListEvent.DeleteThread -> {
                viewModelScope.launch {
                    //threadOperations.deleteThreadUseCase()
                }
                loadThreadList()
            }
            is ThreadListEvent.ReloadThreadList -> {
                loadThreadList()
            }
        }
    }

    private fun loadThreadList() {
        val accessToken = getToken()
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }
            try {
                CoroutineScope(coroutineContext).launch(Dispatchers.IO) {
                    val threadList = threadOperations.loadThreadListUseCase(accessToken).getOrThrow()
                    _state.update {
                        it.copy(
                            isLoading = false,
                            threadList = threadList
                        )
                    }
                }
            } catch (e: Exception) {
                _state.update {
                    it.copy(
                        isLoading = false,
                        error = e
                    )
                }
            }
        }
    }

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val savedStateHandle = createSavedStateHandle()
                val threadOperations = ChatUseCases(ChatApplication.threadsClient)
                ThreadListViewModel(
                    threadOperations = threadOperations,
                    savedStateHandle = savedStateHandle,
                )
            }
        }
    }
}