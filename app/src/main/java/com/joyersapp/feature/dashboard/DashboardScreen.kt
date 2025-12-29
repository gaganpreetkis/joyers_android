package com.joyersapp.feature.dashboard

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController

@Composable
fun DashboardScreen(
    viewModel: AppScaffoldViewModel = viewModel()
) {
    val navController = rememberNavController()
    val uiState by viewModel.uiState.collectAsState()
    // Track current route for bottom bar visibility
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route
    val bottomBarRoutes = BottomTab.entries.map { it.route }

    FloatingBottomNavHost(
        topBar = {},
        showBottomTab = currentRoute in bottomBarRoutes,
        selectedTab = uiState.selectedTab,
        onBottomTabSelected = { tab ->
            viewModel.onBottomTabSelected(tab)
            navController.navigate(tab.route) {
                launchSingleTop = true
//                restoreState = true
                popUpTo(navController.graph.startDestinationId) {
//                    saveState = true
                }
            }
                              },
        content = { innerPadding -> DashboardNavGraph(navController) }
    )
}