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
import hu.bme.aut.android.susssychat.feature.login.LoginScreen
import hu.bme.aut.android.susssychat.feature.thread.message_list.MessageListScreen

@OptIn(ExperimentalMaterial3Api::class, ExperimentalComposeUiApi::class)
@Composable
fun NavGraph(
    navController: NavHostController = rememberNavController(),
) {
    NavHost(
        navController = navController,
        startDestination = Screen.Login.route
    ) {
        composable(Screen.Login.route) {
            LoginScreen(
                onLogin = {
                    navController.navigate(Screen.ThreadList.passToken(it))
                }
            )
        }
        composable(
            Screen.ThreadList.route,
            arguments = listOf(navArgument("accessToken") {
                    type = NavType.StringType
                }
            )
        ) {
            ThreadListScreen(
                onListItemClick = { token, id ->
                    navController.navigate(Screen.Thread.passValues(token, id))
                },
            )
        }
        composable(
            Screen.Thread.route,
            arguments = listOf(navArgument("accessToken") {
                type = NavType.StringType
            }
            )
        ) {
            MessageListScreen(
                onListItemClick = { token, id ->
                    //navController.navigate(Screen.Thread.passValues(token, id))
                },
                onFabClick = { token ->
                    //navController.navigate(Screen.CreateThread.passToken(token))
                },
            )
        }
    }
}