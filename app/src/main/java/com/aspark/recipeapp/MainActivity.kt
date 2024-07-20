package com.aspark.recipeapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.core.view.WindowCompat
import androidx.navigation.compose.rememberNavController
import coil.compose.LocalImageLoader
import coil.imageLoader
import com.aspark.recipeapp.ui.NavGraph
import com.aspark.recipeapp.ui.Screen
import com.aspark.recipeapp.ui.component.BottomNavigationBar
import com.aspark.recipeapp.ui.theme.RecipeAppTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {

//            WindowCompat.setDecorFitsSystemWindows(window, false)

            RecipeAppTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val context = LocalContext.current
                    val navController = rememberNavController()
                    var showBottomNavBar by remember {mutableStateOf(true)}
                    val imageLoader = remember{
                        (context.applicationContext as MyApplication).imageLoader
                    }
                        Scaffold(
                            bottomBar = {
                                if (showBottomNavBar)
                                    BottomNavigationBar(navController = navController)
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
