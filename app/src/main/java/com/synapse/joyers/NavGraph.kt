package com.synapse.joyers

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.synapse.joyers.auth.ForgotPasswordScreen
import com.synapse.joyers.auth.IdentityScreen
import com.synapse.joyers.auth.SignUpScreen
import com.synapse.joyers.auth.LoginScreen
import com.synapse.joyers.auth.ResetPasswordScreen
import com.synapse.joyers.ui.screens.SplashScreen

sealed class Routes(val route: String) {
    data object Splash : Routes("splash")
    data object Login : Routes("login")
    data object SignUp : Routes("signup")
    data object ForgotPassword : Routes("forgotPassword")
    data object Identity : Routes("identity")
    data object ResetPassword : Routes("resetPassword")

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
            ForgotPasswordScreen(
                onLoginClick = {
                    navController.navigate(Routes.Login.route) {
                        popUpTo(0)
                        launchSingleTop = true
                    }
                },
                onNavigateToResetPassword = { identifier, countryCode, countryNameCode, verificationCode ->
                    // URL encode the values to handle special characters in email/phone
                    val encodedIdentifier = java.net.URLEncoder.encode(identifier, "UTF-8")
                    val encodedCountryCode = java.net.URLEncoder.encode(countryCode, "UTF-8")
                    val encodedCountryNameCode = java.net.URLEncoder.encode(countryNameCode, "UTF-8")
                    val encodedVerificationCode = java.net.URLEncoder.encode(verificationCode, "UTF-8")
                    navController.navigate(
                        "${Routes.ResetPassword.route}/$encodedIdentifier/$encodedCountryCode/$encodedCountryNameCode/$encodedVerificationCode"
                    ) {
                        popUpTo(Routes.ForgotPassword.route)
                    }
                }
            )
        }

        composable(
            route = "${Routes.ResetPassword.route}/{identifier}/{countryCode}/{countryNameCode}/{verificationCode}",
            arguments = listOf(
                navArgument("identifier") { type = NavType.StringType },
                navArgument("countryCode") { type = NavType.StringType },
                navArgument("countryNameCode") { type = NavType.StringType },
                navArgument("verificationCode") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            val identifier = backStackEntry.arguments?.getString("identifier")?.let {
                java.net.URLDecoder.decode(it, "UTF-8")
            } ?: ""
            val countryCode = backStackEntry.arguments?.getString("countryCode")?.let {
                java.net.URLDecoder.decode(it, "UTF-8")
            } ?: ""
            val countryNameCode = backStackEntry.arguments?.getString("countryNameCode")?.let {
                java.net.URLDecoder.decode(it, "UTF-8")
            } ?: ""
            val verificationCode = backStackEntry.arguments?.getString("verificationCode")?.let {
                java.net.URLDecoder.decode(it, "UTF-8")
            } ?: ""
            ResetPasswordScreen(
                identifier = identifier,
                countryCode = countryCode,
                countryNameCode = countryNameCode,
                verificationCode = verificationCode,
                onLoginClick = {
                    navController.navigate(Routes.Login.route) {
                        popUpTo(0)
                        launchSingleTop = true
                    }
                },
                onVerifyClick = { newPassword, confirmPassword ->
                    // TODO: Call API to reset password
                    // loginViewModel.resetPassword(...)
                }
            )
        }
    }
}