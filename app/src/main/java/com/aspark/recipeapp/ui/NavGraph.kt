package com.aspark.recipeapp.ui

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material.icons.outlined.Home
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.aspark.recipeapp.ui.screen.FavouriteScreen
import com.aspark.recipeapp.ui.screen.HomeScreen
import com.aspark.recipeapp.ui.screen.RecipeDetailScreen
import com.aspark.recipeapp.ui.screen.SearchScreen

@Composable
fun NavGraph(
    navController: NavHostController,
    innerPadding: PaddingValues,
) {

    NavHost(
        navController = navController,
        startDestination = Screen.Home.route
    ) {

        composable(
            route = Screen.Home.route,
            popEnterTransition = {
                slideIntoContainer(
                    towards = AnimatedContentTransitionScope.SlideDirection.Up,
                    animationSpec = tween(400)
                )
            },
//            popExitTransition = {
//                slideOutOfContainer(
//                    towards = AnimatedContentTransitionScope.SlideDirection.Down,
//                    animationSpec = tween(400)
//                )
//            },
            exitTransition = {
                slideOutOfContainer(
                    towards = AnimatedContentTransitionScope.SlideDirection.Down,
                    animationSpec = tween(400)
                )
            }
            ) {
            HomeScreen(navController, innerPadding)
        }

        composable(
            route = Screen.RecipeDetail.route,
            arguments = listOf(navArgument("recipeId") { type = NavType.LongType })
        ) { backStackEntry ->
            val recipeId = backStackEntry.arguments?.getLong("recipeId") ?: return@composable
            RecipeDetailScreen(recipeId)
        }
        composable(Screen.Favorites.route) {
            FavouriteScreen(navController = navController, innerPadding )
        }

        composable(Screen.Search.route,
            enterTransition = {
                slideIntoContainer(
                    towards = AnimatedContentTransitionScope.SlideDirection.Down,
                    animationSpec = tween(400)
                )
            },
            exitTransition = {
                slideOutOfContainer(
                    towards = AnimatedContentTransitionScope.SlideDirection.Up,
                    animationSpec = tween(400)
                )
            }
            ) {
            SearchScreen(navController = navController)
        }
    }
}

sealed class Screen(
    val route: String,
    val icon: ImageVector?,
    val selectedIcon: ImageVector?
) {
    object Home : Screen("home", Icons.Outlined.Home, Icons.Filled.Home)
    object RecipeDetail : Screen("recipeDetail/{recipeId}",
        null, null
        ) {
        fun createRoute(recipeId: Long) = "recipeDetail/$recipeId"
    }
    object Favorites : Screen("favorites", Icons.Outlined.FavoriteBorder, Icons.Filled.Favorite)
    object Search: Screen("search", null, null)
}