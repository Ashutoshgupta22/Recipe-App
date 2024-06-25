package com.aspark.recipeapp.ui.component

import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.navigation.NavController
import com.aspark.recipeapp.ui.Screen

@Composable
fun BottomNavigationBar(navController: NavController) {

    NavigationBar {

        var selected by remember { mutableStateOf<Screen>(Screen.Home) }

        NavigationBarItem(
            selected = selected == Screen.Home,
            onClick = {
                navController.navigate(Screen.Home.route)
                selected = Screen.Home
                      },
            label = { Text(text = "Home") },
            icon = {
                Icon(
                    imageVector =
                    if (selected == Screen.Home) Screen.Home.selectedIcon
                    else Screen.Home.icon,
                    contentDescription = ""
                )
            }
        )

        NavigationBarItem(
            selected = selected == Screen.Favorites,
            onClick = {
                navController.navigate(Screen.Favorites.route)
                selected = Screen.Favorites
                      },
            label = { Text(text = "Favourite") },
            icon = {
                Icon(
                    imageVector =
                    if (selected == Screen.Favorites) Screen.Favorites.selectedIcon
                    else Screen.Favorites.icon,
                    contentDescription = ""
                )
            }
        )
    }
}