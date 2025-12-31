package com.joyersapp.utils

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.tween
import androidx.navigation.NavBackStackEntry

//@OptIn(ExperimentalAnimationApi::class)
fun AnimatedContentTransitionScope<NavBackStackEntry>.defaultEnterTransition() =
    slideIntoContainer(
        AnimatedContentTransitionScope.SlideDirection.Up,
        animationSpec = tween(500)
    )

//@OptIn(ExperimentalAnimationApi::class)
fun AnimatedContentTransitionScope<NavBackStackEntry>.defaultExitTransition() =
    slideOutOfContainer(
        AnimatedContentTransitionScope.SlideDirection.Up,
        animationSpec = tween(500)
    )

//@OptIn(ExperimentalAnimationApi::class)
fun AnimatedContentTransitionScope<NavBackStackEntry>.defaultPopEnterTransition() =
    slideIntoContainer(
        AnimatedContentTransitionScope.SlideDirection.Down,
        animationSpec = tween(300)
    )

//@OptIn(ExperimentalAnimationApi::class)
fun AnimatedContentTransitionScope<NavBackStackEntry>.defaultPopExitTransition() =
    slideOutOfContainer(
        AnimatedContentTransitionScope.SlideDirection.Down,
        animationSpec = tween(300)
    )