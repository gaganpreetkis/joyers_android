package com.joyersapp.feature.dashboard

import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.runtime.Composable
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.joyersapp.common_widgets.IdentificationDialog
import com.joyersapp.components.dialogs.EditDescriptionDialog
import com.joyersapp.components.dialogs.EditProfileHeaderDialog
import com.joyersapp.components.dialogs.ProfileViewDialog
import com.joyersapp.feature.home.presentation.HomeScreen
import com.joyersapp.feature.profile.presentation.MagneticsScreen
import com.joyersapp.feature.profile.presentation.UserProfileScreen
import com.joyersapp.feature.profile.presentation.UserProfileViewModel
import com.joyersapp.utils.defaultEnterTransition
import com.joyersapp.utils.defaultExitTransition
import com.joyersapp.utils.defaultPopEnterTransition
import com.joyersapp.utils.defaultPopExitTransition


sealed class Routes(val route: String) {
    data object Magnetics : Routes("magnetics")
    data object ProfileHeaderDialog : Routes("profile_header_dialog")
    data object IdentificationDialog : Routes("identification_dialog")
    data object DescriptionDialog : Routes("description_dialog")
    data object ProfileViewDialog : Routes("profile_view_dialog")

}

@Composable
fun DashboardNavGraph(navController: NavHostController) {

    val userProfileViewModel = hiltViewModel<UserProfileViewModel>()
    val bottomRoutes = BottomTab.entries.map { it.route }
    NavHost(
        navController = navController,
        startDestination = BottomTab.HOME.route,
        enterTransition = {
            if (initialState.destination.route in bottomRoutes &&
                targetState.destination.route in bottomRoutes
            ) {
                fadeIn(animationSpec = tween(0))
            } else {
                defaultEnterTransition()
            }
        },
//        exitTransition = {
//            if (initialState.destination.route in bottomRoutes &&
//                targetState.destination.route in bottomRoutes
//            ) {
//                fadeOut(animationSpec = tween(0))
//            } else {
//                defaultExitTransition()
//            }
//        },
        popEnterTransition = {
            if (initialState.destination.route in bottomRoutes &&
                targetState.destination.route in bottomRoutes
            ) {
                fadeIn(animationSpec = tween(0))
            } else {
                defaultPopEnterTransition()
            }
        },
//        popExitTransition = {
//            if (initialState.destination.route in bottomRoutes &&
//                targetState.destination.route in bottomRoutes
//            ) {
//                fadeOut(animationSpec = tween(0))
//            } else {
//                defaultPopExitTransition()
//            }
//        }
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
                },
//                navigateToIdentificationDialog = { navController.navigate(Routes.IdentificationDialog.route) },
//                navigateToDescriptionDialog = { navController.navigate(Routes.DescriptionDialog.route) },
            )
        }
        composable(Routes.Magnetics.route) {
            MagneticsScreen(
                viewModel = userProfileViewModel,
                onBack = { navController.popBackStack() },
//                navigateToProfileHeader = { navController.navigate(Routes.ProfileHeaderDialog.route) },
//                navigateToDescription = { navController.navigate(Routes.DescriptionDialog.route) },
//                navigateToIdentification = { navController.navigate(Routes.IdentificationDialog.route) },
            )
        }
        composable(Routes.ProfileHeaderDialog.route) {
            EditProfileHeaderDialog(
                onDismiss = { navController.popBackStack() },
                onApply = { navController.popBackStack() }
            )
        }
//        composable(Routes.IdentificationDialog.route) {
//            IdentificationDialog (
//                onDismiss = { navController.popBackStack() },
////                navigateToDescriptionDialog = { navController.navigate(Routes.DescriptionDialog.route) }
//            )
//        }
//        composable(Routes.DescriptionDialog.route) {
//            EditDescriptionDialog(
//                onDismiss = { navController.popBackStack() },
//                onApply = { navController.popBackStack() }
//            )
//        }
//        composable(Routes.ProfileViewDialog.route) {
//            ProfileViewDialog (
//                onDismiss = { navController.popBackStack() },
//                onApply = { navController.popBackStack() }
//            )
//        }



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