package com.synapse.joyers

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.synapse.joyers.auth.SignUpScreen
import com.synapse.joyers.auth.LoginScreen
import com.synapse.joyers.ui.screens.SplashScreen

sealed class Routes(val route: String) {
    data object Splash : Routes("splash")
    data object Login : Routes("login")
    data object SignUp : Routes("signup")
}

@Composable
fun AppNavGraph(navController: NavHostController) {
    NavHost(
        navController = navController,
        startDestination = Routes.Splash.route
    ) {

        composable(Routes.Splash.route) {
            SplashScreen(onNavigateToLogin = {
                navController.navigate(Routes.Login.route) {
                    popUpTo(Routes.Splash.route) { inclusive = true }
                }
            })
        }

        composable(Routes.Login.route) {
            LoginScreen(onSignUpClick = {
                navController.navigate(Routes.SignUp.route) 
            })

        }

        composable(Routes.SignUp.route) {
            SignUpScreen(
//                isValidUsername = { isValidUsername(it) }
            )
        }
    }
}

fun isValidUsername(username: String): Boolean {
    val regex = "^[a-zA-Z0-9_]{3,15}$".toRegex()
    return username.matches(regex)
}

