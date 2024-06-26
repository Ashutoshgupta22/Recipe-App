package com.aspark.recipeapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import com.aspark.recipeapp.ui.NavGraph
import com.aspark.recipeapp.ui.component.BottomNavigationBar
import com.aspark.recipeapp.ui.theme.RecipeAppTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            RecipeAppTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
//
                    val navController = rememberNavController()

                    Scaffold(
                        bottomBar = {
                            BottomNavigationBar(navController = navController)
                        }
                    ) {
                        NavGraph( navController, it)
                    }
                }
            }
        }
    }
}
