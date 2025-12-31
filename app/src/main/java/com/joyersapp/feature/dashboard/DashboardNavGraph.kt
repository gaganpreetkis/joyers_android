package com.joyersapp.feature.dashboard

import androidx.compose.runtime.Composable
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.joyersapp.feature.home.presentation.HomeScreen
import com.joyersapp.feature.profile.presentation.MagneticsScreen
import com.joyersapp.feature.profile.presentation.UserProfileScreen
import com.joyersapp.feature.profile.presentation.UserProfileViewModel


sealed class Routes(val route: String) {
    data object Magnetics : Routes("magnetics")

}

@Composable
fun DashboardNavGraph(navController: NavHostController) {

    val userProfileViewModel = hiltViewModel<UserProfileViewModel>()
    NavHost(
        navController = navController,
        startDestination = BottomTab.HOME.route
    ) {

        composable(BottomTab.HOME.route) {
            HomeScreen(
//                onOpenPost = {
//                    navController.navigate(HomeRoutes.postDetails(it))
//                }
            )
        }

        // PROFILE
        composable(BottomTab.PROFILE.route) {
            UserProfileScreen(
                viewModel = userProfileViewModel,
                editMagnetics = {
                    navController.navigate(Routes.Magnetics.route)
                }
            )
        }
        composable(Routes.Magnetics.route) {
            MagneticsScreen(
                viewModel = userProfileViewModel,
                onBack = { navController.popBackStack() }
            )
        }

        composable(BottomTab.POST.route) {
            HomeScreen()
        }

        composable(BottomTab.CONTACTS.route) {
            HomeScreen()
        }

        composable(BottomTab.NOTIFICATIONS.route) {
            HomeScreen()
        }

    }
}