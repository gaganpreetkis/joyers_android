package com.joyersapp.feature.dashboard

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.joyersapp.feature.home.presentation.HomeScreen
import com.joyersapp.feature.profile.presentation.UserProfileScreen

@Composable
fun DashboardNavGraph(navController: NavHostController) {

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

        composable(BottomTab.PROFILE.route) {
            UserProfileScreen()
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