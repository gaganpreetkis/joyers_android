package com.joyersapp.feature.dashboard

import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.runtime.Composable
import androidx.compose.ui.window.DialogProperties
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.dialog
import com.joyersapp.auth.presentation.identity.IdentityScreen
import com.joyersapp.components.dialogs.DescriptionDialog
import com.joyersapp.components.dialogs.EditProfileHeaderDialog
import com.joyersapp.components.dialogs.MentionJoyersDialog
import com.joyersapp.feature.home.presentation.HomeScreen
import com.joyersapp.feature.profile.data.remote.dto.ProfileMeta
import com.joyersapp.feature.profile.data.remote.dto.ProfileTitlesData
import com.joyersapp.feature.profile.presentation.MagneticsScreen
import com.joyersapp.feature.profile.presentation.UserProfileEvent
import com.joyersapp.feature.profile.presentation.UserProfileScreen
import com.joyersapp.feature.profile.presentation.UserProfileViewModel
import com.joyersapp.utils.defaultEnterTransition
import com.joyersapp.utils.defaultPopEnterTransition


sealed class Routes(val route: String) {
    data object Magnetics : Routes("magnetics")
    data object ProfileHeaderDialog : Routes("profile_header_dialog")
    data object MentionJoyersDialog : Routes("mention_joyers_dialog")
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

        // MAGNETICS SCREEN
        composable(Routes.Magnetics.route) {
            MagneticsScreen(
                viewModel = userProfileViewModel,
                onBack = { navController.popBackStack() },

                navigateToDescriptionDialog = {

                    navController.navigate(Routes.DescriptionDialog.route)
                    navController
                        .getBackStackEntry(Routes.DescriptionDialog.route)
                        .savedStateHandle
                        .set(
                            "description_titles_list",
                            it
                        )
                },
                navigateToProfileHeaderDialog = { navController.navigate(Routes.ProfileHeaderDialog.route) },
                navigateToMentionJoyersDialog = { navController.navigate(Routes.MentionJoyersDialog.route) },
//                navigateToIdentification = { navController.navigate(Routes.IdentificationDialog.route) },
            )
        }


        // MENTION JOYERS DIALOG
        dialog (
            route = Routes.MentionJoyersDialog.route,
            dialogProperties = DialogProperties(
                dismissOnBackPress = true,
                dismissOnClickOutside = false,
                usePlatformDefaultWidth = false,
                decorFitsSystemWindows = false
            )
        ) {
            MentionJoyersDialog (
                initList = userProfileViewModel.uiState.value.editMagneticsUserList,
                onDismiss = { navController.popBackStack() },
                onApply = { userProfileViewModel.onEvent(UserProfileEvent.OnApplyMentionedJoyers(it)) },
            )
        }

        // PROFILE HEADER DIALOG
        dialog (
            route = Routes.ProfileHeaderDialog.route,
            dialogProperties = DialogProperties(
                dismissOnBackPress = true,
                dismissOnClickOutside = false,
                usePlatformDefaultWidth = false,
                decorFitsSystemWindows = false
        )
        ) {
            EditProfileHeaderDialog(
                onDismiss = { navController.popBackStack() },
                onApply = { navController.popBackStack() },
                navigateToMentionJoyersDialog = { navController.navigate(Routes.MentionJoyersDialog.route) },

            )
        }

//        composable(Routes.IdentificationDialog.route) {
//            IdentificationDialog (
//                onDismiss = { navController.popBackStack() },
////                navigateToDescriptionDialog = { navController.navigate(Routes.DescriptionDialog.route) }
//            )
//        }

        dialog (
            route = Routes.DescriptionDialog.route,
            dialogProperties = DialogProperties(
                dismissOnBackPress = true,
                dismissOnClickOutside = false,
                usePlatformDefaultWidth = false,
                decorFitsSystemWindows = false
            )
        ) {
            DescriptionDialog (
                initList = userProfileViewModel.uiState.value.titles,
                selectedTitle = ProfileTitlesData(
                    id = userProfileViewModel.uiState.value.magneticsData.title?.id?:"",
                    name = userProfileViewModel.uiState.value.magneticsData.title?.name?:"",
                ),
                selectedSubTitle = ProfileTitlesData(
                    id = userProfileViewModel.uiState.value.magneticsData.subTitle?.id,
                    name = userProfileViewModel.uiState.value.magneticsData.subTitle?.name,
                ),
                onDismiss = { navController.popBackStack() },
                onApply = { title, subTitle ->
//                    navController.navigate(Routes.ProfileHeaderDialog.route)
                    navController.popBackStack()
                    userProfileViewModel.onEvent(UserProfileEvent.OnApplyDescription(title, subTitle))
                }
            )
        }
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
//            IdentityScreen("test", "test")
        }

        composable(BottomTab.NOTIFICATIONS.route) {
            HomeScreen()
        }

    }
}