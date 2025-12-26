package com.joyersapp.feature.dashboard

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.joyersapp.feature.home.presentation.HomeScreen
import com.joyersapp.feature.profile.presentation.MagneticsScreen
import com.joyersapp.feature.profile.presentation.UserProfileScreen


sealed class Routes(val route: String) {
    data object Home : Routes("home")
    data object Profile : Routes("profile")
    data object Magnetics : Routes("magnetics")
    data object Post : Routes("post")
    data object Contacts : Routes("contacts")
    data object Notifications : Routes("notifications")


}

@Composable
fun DashboardNavGraph(navController: NavHostController) {

    NavHost(
        navController = navController,
        startDestination = Routes.Home.route
    ) {

        composable(Routes.Home.route) {
            HomeScreen(
//                onOpenPost = {
//                    navController.navigate(HomeRoutes.postDetails(it))
//                }
            )
        }

        // PROFILE
        composable(Routes.Profile.route) {
            UserProfileScreen(
                editMagnetics = {
                    navController.navigate(Routes.Magnetics.route)
                }
            )
        }
        composable(Routes.Magnetics.route) {
            MagneticsScreen(
                username = "Sara_99",
                onBack = { navController.popBackStack() }


            )
        }

        composable(Routes.Post.route) {
            HomeScreen()
        }

        composable(Routes.Contacts.route) {
            HomeScreen()
        }

        composable(Routes.Notifications.route) {
            HomeScreen()
        }

    }
}