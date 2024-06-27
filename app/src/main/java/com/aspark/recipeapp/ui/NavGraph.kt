package com.aspark.recipeapp.ui

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.outlined.Favorite
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material.icons.outlined.Home
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.aspark.recipeapp.ui.screen.FavouriteScreen
import com.aspark.recipeapp.ui.screen.HomeScreen
import com.aspark.recipeapp.ui.screen.RecipeDetailScreen

@Composable
fun NavGraph(
    navController: NavHostController,
    paddingValues: PaddingValues,
    onNavigate: (String) -> Unit
) {

    NavHost(
        navController = navController,
        startDestination = Screen.Home.route
    ) {

        composable(Screen.Home.route) {
            HomeScreen(navController)
            onNavigate(Screen.Home.route)
        }

        composable(
            route = Screen.RecipeDetail.route,
            arguments = listOf(navArgument("recipeId") { type = NavType.LongType })
        ) { backStackEntry ->
            val recipeId = backStackEntry.arguments?.getLong("recipeId") ?: return@composable
            RecipeDetailScreen(recipeId)
            onNavigate(Screen.RecipeDetail.route)
        }
        composable(Screen.Favorites.route) {
            FavouriteScreen(navController = navController)
            onNavigate(Screen.Favorites.route)
        }
    }
}

sealed class Screen(
    val route: String,
    val icon: ImageVector,
    val selectedIcon: ImageVector
) {
    object Home : Screen("home", Icons.Outlined.Home, Icons.Filled.Home)
    object RecipeDetail : Screen("recipeDetail/{recipeId}",
        Icons.Outlined.Home, Icons.Filled.Home
        ) {
        fun createRoute(recipeId: Long) = "recipeDetail/$recipeId"
    }
    object Favorites : Screen("favorites", Icons.Outlined.FavoriteBorder, Icons.Filled.Favorite)
}