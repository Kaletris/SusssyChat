package hu.bme.aut.android.susssychat.navigation

sealed class Screen(val route: String) {
    object Login: Screen("login")
    object ThreadList: Screen("threads/{accessToken}") {
        fun passToken(token: String) = "threads/$token"
    }

    object Thread: Screen("threads/{accessToken}/{id}"){
        fun passValues(token: String, id: Int) = "threads/$token/$id"
    }
    object CreateThread: Screen("threads/{accessToken}/create") {
        fun passToken(token: String) = "threads/$token/create"
    }
}