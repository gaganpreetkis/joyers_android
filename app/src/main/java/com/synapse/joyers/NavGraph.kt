package com.synapse.joyers

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.synapse.joyers.auth.ForgotPasswordScreen
import com.synapse.joyers.auth.IdentityScreen
import com.synapse.joyers.auth.SignUpScreen
import com.synapse.joyers.auth.LoginScreen
import com.synapse.joyers.ui.screens.SplashScreen

sealed class Routes(val route: String) {
    data object Splash : Routes("splash")
    data object Login : Routes("login")
    data object SignUp : Routes("signup")
    data object ForgotPassword : Routes("forgotPassword")
    data object Identity : Routes("identity")
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
            }, onForgotPasswordClick = {
                navController.navigate(Routes.ForgotPassword.route)
            })

        }

        composable(Routes.SignUp.route) {
            SignUpScreen(
//                isValidUsername = { isValidUsername(it) },
                onSignUpClick = {
                    navController.navigate(Routes.Identity.route)
                },
                onLogInClick = {
                    navController.navigate(Routes.Login.route)
                }
            )
        }

        composable(Routes.Identity.route) {
            IdentityScreen(0) { }
        }

        composable(Routes.ForgotPassword.route) {
            ForgotPasswordScreen(onLoginClick = {
                navController.navigate(Routes.Login.route) {
                    popUpTo(Routes.ForgotPassword.route) { inclusive = true }
                }
            })
        }
    }
}

fun isValidUsername(username: String): Boolean {
    val regex = "^[a-zA-Z0-9_]{3,15}$".toRegex()
    return username.matches(regex)
}

