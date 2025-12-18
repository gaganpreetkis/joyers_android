package com.joyersapp

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.joyersapp.auth.presentation.forgotpassword.ForgotPasswordScreen
import com.joyersapp.auth.presentation.identity.IdentityScreen
import com.joyersapp.auth.presentation.JoyersOathScreen
import com.joyersapp.auth.presentation.login.LoginScreen
import com.joyersapp.auth.presentation.resetpassword.ResetPasswordScreen
import com.joyersapp.auth.presentation.signup.SignUpScreen
import com.joyersapp.auth.presentation.SplashVideoScreen
import com.joyersapp.feature.common.AppScaffoldRoot
import com.synapse.joyers.ui.screens.SplashScreen
import java.net.URLDecoder
import java.net.URLEncoder

sealed class Routes(val route: String) {
    data object Splash : Routes("splash")
    data object Login : Routes("login")
    data object SignUp : Routes("signup")
    data object ForgotPassword : Routes("forgotPassword")
    data object Identity : Routes("identity")
    data object ResetPassword : Routes("resetPassword")
    data object JoyersOath : Routes("joyersOath")
    data object SplashVideo : Routes("splashVideo")
    data object Dashboard : Routes("dashboard")

}

@Composable
fun AppNavGraph(navController: NavHostController) {
    NavHost(
        navController = navController,
        startDestination = Routes.Dashboard.route
//        startDestination = "${Routes.Identity.route}/test/test"
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
            }, onLoginSuccess = {
                navController.navigate(Routes.SplashVideo.route) {
                    popUpTo(0)
                    launchSingleTop = true
                }
            })

        }

        composable(Routes.SignUp.route) {
            SignUpScreen(
//                isValidUsername = { isValidUsername(it) },
                onSignUpClick = { token, userId ->
                    val encodedUserId = URLEncoder.encode(userId, "UTF-8")
                    val encodedToken = URLEncoder.encode(token, "UTF-8")

                    navController.navigate("${Routes.Identity.route}/$encodedUserId/$encodedToken")
                },
                onLogInClick = {
                    navController.navigate(Routes.Login.route) {
                        popUpTo(0)
                        launchSingleTop = true
                    }
                }
            )
        }

        composable(
            route = "${Routes.Identity.route}/{userId}/{token}",
            arguments = listOf(
                navArgument("userId") { type = NavType.StringType },
                navArgument("token") { type = NavType.StringType }
            )
        ) { backStackEntry ->

            val userId = backStackEntry.arguments?.getString("userId")?.let {
                URLDecoder.decode(it, "UTF-8")
            } ?: ""

            val token = backStackEntry.arguments?.getString("token")?.let {
                URLDecoder.decode(it, "UTF-8")
            } ?: ""

            IdentityScreen(
                userId = userId,
                token = token,
                onNavigateToNext = {
                    navController.navigate(Routes.JoyersOath.route) {
                        popUpTo(0)
                        launchSingleTop = true
                    }
                }
            )
        }

        /*composable(Routes.Identity.route) {
            IdentityScreen(0, onNavigateToNext = {
                navController.navigate(Routes.JoyersOath.route) {
                    popUpTo(0)
                    launchSingleTop = true
                }
            })
        }*/

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
                    val encodedIdentifier = URLEncoder.encode(identifier, "UTF-8")
                    val encodedCountryCode = URLEncoder.encode(countryCode, "UTF-8")
                    val encodedCountryNameCode = URLEncoder.encode(countryNameCode, "UTF-8")
                    val encodedVerificationCode = URLEncoder.encode(verificationCode, "UTF-8")
                    navController.navigate(
                        "${Routes.ResetPassword.route}/$encodedIdentifier/$encodedCountryCode/$encodedCountryNameCode/$encodedVerificationCode"
                    ) {
                        popUpTo(Routes.ForgotPassword.route)
                    }
                },
                initialPhoneMode = false
            )
        }

        composable(
            route = "${Routes.ForgotPassword.route}?phoneMode={phoneMode}",
            arguments = listOf(
                navArgument("phoneMode") {
                    type = NavType.BoolType
                }
            )
        ) { backStackEntry ->
            val phoneMode = backStackEntry.arguments?.getBoolean("phoneMode") ?: false
            ForgotPasswordScreen(
                onLoginClick = {
                    navController.navigate(Routes.Login.route) {
                        popUpTo(0)
                        launchSingleTop = true
                    }
                },
                onNavigateToResetPassword = { identifier, countryCode, countryNameCode, verificationCode ->
                    // URL encode the values to handle special characters in email/phone
                    val encodedIdentifier = URLEncoder.encode(identifier, "UTF-8")
                    val encodedCountryCode = URLEncoder.encode(countryCode, "UTF-8")
                    val encodedCountryNameCode = URLEncoder.encode(countryNameCode, "UTF-8")
                    val encodedVerificationCode = URLEncoder.encode(verificationCode, "UTF-8")
                    navController.navigate(
                        "${Routes.ResetPassword.route}/$encodedIdentifier/$encodedCountryCode/$encodedCountryNameCode/$encodedVerificationCode"
                    ) {
                        popUpTo(Routes.ForgotPassword.route)
                    }
                },
                initialPhoneMode = phoneMode
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
                URLDecoder.decode(it, "UTF-8")
            } ?: ""
            val countryCode = backStackEntry.arguments?.getString("countryCode")?.let {
                URLDecoder.decode(it, "UTF-8")
            } ?: ""
            val countryNameCode = backStackEntry.arguments?.getString("countryNameCode")?.let {
                URLDecoder.decode(it, "UTF-8")
            } ?: ""
            val verificationCode = backStackEntry.arguments?.getString("verificationCode")?.let {
                URLDecoder.decode(it, "UTF-8")
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
                onLoginSuccess = {
                    navController.navigate(Routes.SplashVideo.route) {
                        popUpTo(0)
                        launchSingleTop = true
                    }
                },
                onVerifyClick = { newPassword, confirmPassword ->
                    // TODO: Call API to reset password
                    // loginViewModel.resetPassword(...)
                },
                onBackToForgotPassword = { phoneMode ->
                    navController.navigate("${Routes.ForgotPassword.route}?phoneMode=$phoneMode") {
                        popUpTo(Routes.ForgotPassword.route) { inclusive = true }
                        launchSingleTop = false
                    }
                }
            )
        }

        composable(Routes.JoyersOath.route) {
            JoyersOathScreen(
                onFabClick = {
                    navController.navigate(Routes.SplashVideo.route) {
                        popUpTo(0) { inclusive = true }
                    }
                }
            )
        }

        composable(Routes.SplashVideo.route) {
            SplashVideoScreen(
                onNavigateToDashboard = { context ->
                    // Show toast message
                    //Toast.makeText(context, "Navigating to Dashboard", Toast.LENGTH_SHORT).show()
                    navController.navigate(Routes.Dashboard.route) {
                        popUpTo(0) { inclusive = true }
                    }
                    // Navigate to DashboardActivity (Activity, not Compose screen)
                    /*val intent = android.content.Intent(context, com.synapse.joyers.ui.dashboard.DashboardActivity::class.java)
                    context.startActivity(intent)
                    (context as? android.app.Activity)?.finish()*/
                }
            )
        }

        composable(Routes.Dashboard.route) {
            AppScaffoldRoot()
        }
    }
}