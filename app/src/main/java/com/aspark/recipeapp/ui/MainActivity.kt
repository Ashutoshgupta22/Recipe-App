package com.aspark.recipeapp.ui

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import com.aspark.recipeapp.ui.component.BottomNavigationBar
import com.aspark.recipeapp.ui.theme.RecipeAppTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            RecipeAppTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val navController = rememberNavController()
                    var showBottomNavBar by remember {mutableStateOf(true)}
                    var selected by remember { mutableStateOf<Screen>(Screen.Home) }

                    Log.i("MainActivity", "onCreate: called ")

                        Scaffold(
                            bottomBar = {
                                if (showBottomNavBar)
                                    BottomNavigationBar(
                                        navController = navController,
                                        selected = selected
                                    ) { selectedScreen ->
                                        selected = selectedScreen
                                    }
                            }
                        ) {
                            NavGraph(navController, it) { route ->
                                showBottomNavBar = route == Screen.Home.route
                                        || route == Screen.Favorites.route
                            }
                        }
                }
            }
        }
    }
}