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
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.aspark.recipeapp.ui.screen.FavouriteScreen
import com.aspark.recipeapp.ui.screen.HomeScreen

@Composable
fun NavGraph(
    navController: NavHostController,
    paddingValues: PaddingValues
) {

    NavHost(
        navController = navController,
        startDestination = Screen.Home.route
    ) {

        composable(Screen.Home.route) {
            HomeScreen()
        }
        composable(Screen.RecipeDetail.route) {

        }
        composable(Screen.Favorites.route) {
            FavouriteScreen(navController = navController)
        }
    }

}

sealed class Screen(
    val route: String,
    val icon: ImageVector,
    val selectedIcon: ImageVector
) {
    object Home : Screen("home", Icons.Outlined.Home, Icons.Filled.Home)
    object RecipeDetail : Screen("recipeDetail", Icons.Outlined.Home, Icons.Filled.Home)
    object Favorites : Screen("favorites", Icons.Outlined.FavoriteBorder, Icons.Filled.Favorite)
}