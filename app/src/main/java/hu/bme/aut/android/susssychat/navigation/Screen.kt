package hu.bme.aut.android.susssychat.navigation

sealed class Screen(val route: String) {
    object Login: Screen("login")
    object ThreadList: Screen("threads")
    object Thread: Screen("threads/{id}"){
        fun passId(id: Int) = "threads/$id"
    }
    object CreateThread: Screen("threads/create")
}