package hu.bme.aut.android.susssychat.navigation

sealed class Screen(val route: String) {
    object Login: Screen("login")
    object ThreadList: Screen("threads")
    object Thread: Screen("threads/{id}")
}