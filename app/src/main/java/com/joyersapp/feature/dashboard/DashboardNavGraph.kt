package com.joyersapp.feature.dashboard

import android.app.Activity
import androidx.activity.compose.BackHandler
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.window.DialogProperties
import androidx.compose.ui.window.Popup
import androidx.compose.ui.window.PopupProperties
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.dialog
import com.joyersapp.auth.presentation.identity.IdentityScreen
import com.joyersapp.components.dialogs.DescriptionDialog
import com.joyersapp.components.dialogs.EditProfileHeaderDialog
import com.joyersapp.components.dialogs.MentionJoyersDialog
import com.joyersapp.feature.home.presentation.HomeScreen
import com.joyersapp.feature.post.presentation.create_joy.CreateJoyScreen
import com.joyersapp.feature.post.presentation.create_joy.CreateJoyViewModel
import com.joyersapp.feature.post.presentation.create_post.CreatePostScreen
import com.joyersapp.feature.post.presentation.create_post.CreatePostViewModel
import com.joyersapp.feature.post.presentation.create_post.MediaPreviewScreen
import com.joyersapp.feature.profile.data.remote.dto.ProfileTitlesData
import com.joyersapp.feature.profile.presentation.MagneticsScreen
import com.joyersapp.feature.profile.presentation.UserProfileEvent
import com.joyersapp.feature.profile.presentation.UserProfileScreen
import com.joyersapp.feature.profile.presentation.UserProfileViewModel
import com.joyersapp.utils.defaultEnterTransition
import com.joyersapp.utils.defaultPopEnterTransition
import com.joyersapp.utils.noRippleClickable


sealed class ProfileRoutes(val route: String) {
    data object Magnetics : ProfileRoutes("magnetics")
    data object ProfileHeaderDialog : ProfileRoutes("profile_header_dialog")
    data object MentionJoyersDialog : ProfileRoutes("mention_joyers_dialog")
    data object IdentificationDialog : ProfileRoutes("identification_dialog")
    data object DescriptionDialog : ProfileRoutes("description_dialog")
    data object ProfileViewDialog : ProfileRoutes("profile_view_dialog")

}
sealed class JoyRoutes(val route: String) {
    data object CreatePost : JoyRoutes("create_post")
    data object PreviewMedia : JoyRoutes("preview_media")

}

@Composable
fun DashboardNavGraph(navController: NavHostController) {
    val context = LocalContext.current
    BackHandler(enabled = true) {
        (context as? Activity)?.finish()
    }

    val userProfileViewModel = hiltViewModel<UserProfileViewModel>()
    val createJoyViewmodel = hiltViewModel<CreateJoyViewModel>()
    val createPostViewmodel = hiltViewModel<CreatePostViewModel>()
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

        // HOME TAB

        composable(BottomTab.HOME.route) {
            HomeScreen(
//                onOpenPost = {
//                    navController.navigate(HomeRoutes.postDetails(it))
//                }
            )
        }

        // PROFILE TAB

        composable(BottomTab.PROFILE.route) {
            UserProfileScreen(
                viewModel = userProfileViewModel,
                editMagnetics = {
//                    navController.navigate("test")
                    navController.navigate(ProfileRoutes.Magnetics.route)
                },
//                navigateToIdentificationDialog = { navController.navigate(ProfileRoutes.IdentificationDialog.route) },
//                navigateToDescriptionDialog = { navController.navigate(ProfileRoutes.DescriptionDialog.route) },
            )
        }

        // MAGNETICS SCREEN
        composable(ProfileRoutes.Magnetics.route) {
            MagneticsScreen(
                viewModel = userProfileViewModel,
                onBack = { navController.popBackStack() },

                navigateToDescriptionDialog = {

                    navController.navigate(ProfileRoutes.DescriptionDialog.route)
                    navController
                        .getBackStackEntry(ProfileRoutes.DescriptionDialog.route)
                        .savedStateHandle
                        .set(
                            "description_titles_list",
                            it
                        )
                },
                navigateToProfileHeaderDialog = { navController.navigate(ProfileRoutes.ProfileHeaderDialog.route) },
                navigateToMentionJoyersDialog = { navController.navigate(ProfileRoutes.MentionJoyersDialog.route) },
//                navigateToIdentification = { navController.navigate(ProfileRoutes.IdentificationDialog.route) },
            )
        }


        // MENTION JOYERS DIALOG
        dialog (
            route = ProfileRoutes.MentionJoyersDialog.route,
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
            route = ProfileRoutes.ProfileHeaderDialog.route,
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
                navigateToMentionJoyersDialog = { navController.navigate(ProfileRoutes.MentionJoyersDialog.route) },

            )
        }

//        composable(ProfileRoutes.IdentificationDialog.route) {
//            IdentificationDialog (
//                onDismiss = { navController.popBackStack() },
////                navigateToDescriptionDialog = { navController.navigate(ProfileRoutes.DescriptionDialog.route) }
//            )
//        }

        dialog (
            route = ProfileRoutes.DescriptionDialog.route,
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
//                    navController.navigate(ProfileRoutes.ProfileHeaderDialog.route)
                    navController.popBackStack()
                    userProfileViewModel.onEvent(UserProfileEvent.OnApplyDescription(title, subTitle))
                }
            )
        }
//        composable(ProfileRoutes.ProfileViewDialog.route) {
//            ProfileViewDialog (
//                onDismiss = { navController.popBackStack() },
//                onApply = { navController.popBackStack() }
//            )
//        }

        composable("test") {
            var showPopup by remember { mutableStateOf(false) }

            Box(Modifier.fillMaxSize()) {
                Button(onClick = { showPopup = true }) {
                    Text("Open Full-Screen Popup")
                }

                if (showPopup) {
                    FullScreenPopupOverlay(onDismiss = { showPopup = false }) {
                        Text("I am a full-screen popup!")
                    }
                }
            }
        }

        // CREATE JOY/POST TAB

        composable(BottomTab.POST.route) {
            CreateJoyScreen(
                viewModel = createJoyViewmodel,
                navCreatePost = {
                    navController.navigate(JoyRoutes.CreatePost.route)
                }
            )
        }

        composable(JoyRoutes.CreatePost.route) {
            CreatePostScreen(
                sharedViewmodel = createJoyViewmodel,
                viewmodel = createPostViewmodel,
                onBack = { navController.popBackStack() },
                onPreviewMedia = { navController.navigate(JoyRoutes.PreviewMedia.route) },
            )
        }

        composable(JoyRoutes.PreviewMedia.route) {
            MediaPreviewScreen(
                mediaList = createPostViewmodel.uiState.collectAsStateWithLifecycle().value.mediaList,
                initialPage = 0,
                onBack = { navController.popBackStack() },
            )
        }



        // CONTACTS TAB

        composable(BottomTab.CONTACTS.route) {
//            HomeScreen()
            IdentityScreen("test", "test")
        }


        // NOTIFICATIONS TAB

        composable(BottomTab.NOTIFICATIONS.route) {
            HomeScreen()
        }

    }
}

@Composable
fun FullScreenPopupOverlay(
    onDismiss: () -> Unit,
    content: @Composable () -> Unit
) {
    Popup(
        onDismissRequest = onDismiss,
        properties = PopupProperties(
            focusable = true,
            dismissOnBackPress = true
        )
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black.copy(alpha = 0.5f)) // The full-screen background
                .noRippleClickable(onClick = onDismiss),
            contentAlignment = Alignment.Center
        ) {
            Box(Modifier.clickable(enabled = false) { }) {
                content()
            }
        }
    }
}