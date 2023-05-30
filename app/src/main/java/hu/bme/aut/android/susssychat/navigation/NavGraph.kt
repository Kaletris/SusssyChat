package hu.bme.aut.android.susssychat.navigation

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import hu.bme.aut.android.susssychat.clients.TokenClient
import hu.bme.aut.android.susssychat.feature.thread_list.ThreadListScreen
import hu.bme.aut.android.susssychat.login.LoginScreen

@OptIn(ExperimentalMaterial3Api::class, ExperimentalComposeUiApi::class)
@Composable
fun NavGraph(
    navController: NavHostController = rememberNavController(),
    tokenClient: TokenClient,

    ) {
    var accessToken by rememberSaveable { mutableStateOf("") }

    NavHost(
        navController = navController,
        startDestination = Screen.Login.route
    ) {
        composable(Screen.Login.route) {
            LoginScreen(tokenClient, onLogin = {
                accessToken = it
            })
        }
        composable(Screen.ThreadList.route) {
            ThreadListScreen()
        }
        composable(
            route = Screen.CheckTodo.route,
            arguments = listOf(
                navArgument("id") {
                    type = NavType.IntType
                }
            )
        ) {
            CheckTodoScreen(
                onNavigateBack = {
                    navController.popBackStack(
                        route = Screen.Todos.route,
                        inclusive = true
                    )
                    navController.navigate(Screen.Todos.route)
                }
            )
        }
    }
}