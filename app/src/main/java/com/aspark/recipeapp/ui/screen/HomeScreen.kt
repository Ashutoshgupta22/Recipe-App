package com.aspark.recipeapp.ui.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.rounded.FavoriteBorder
import androidx.compose.material.icons.rounded.Home
import androidx.compose.material.icons.rounded.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SearchBar
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.capitalize
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.aspark.recipeapp.R
import com.aspark.recipeapp.model.Recipe
import com.aspark.recipeapp.ui.NavGraph
import com.aspark.recipeapp.ui.Screen
import com.aspark.recipeapp.ui.component.BottomNavigationBar
import com.aspark.recipeapp.ui.theme.RecipeAppTheme

@Composable
fun HomeScreen(navController: NavController) {

    Scaffold(
        bottomBar = {
            BottomNavigationBar(navController = navController)
        }
    ) {
        HomeScreenContent(it)
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreenContent(paddingValues: PaddingValues) {

    Column(
        modifier = Modifier.padding(16.dp)
    ) {

        Text(text = "\uD83D\uDC4B Hey John", fontWeight = FontWeight.Medium)
        Text(text = "Discover tasty and healthy recipe", fontSize = 12.sp)

        SearchBar(
            query = "",
            onQueryChange ={

            } ,
            onSearch = {

            },
            active = false,
            onActiveChange = {

            },
            placeholder = {
                Text(text = "Search any recipe")
            },
            leadingIcon = {
                Icon(imageVector = Icons.Rounded.Search, contentDescription = "")
            },
            shape = RoundedCornerShape(12.dp)

        ) {
            
        }

        val popularRecipes = listOf(
            Recipe(0,"First",""),
            Recipe(0,"First",""),
            Recipe(0,"First",""),
            Recipe(0,"First",""),
            Recipe(0,"First",""),
        )

        LazyColumn {
            item(){
                PopularRecipes(popularRecipes)
            }

            item { AllRecipes() }
        }

    }
}

@Composable
fun AllRecipes() {


}

@Composable
fun PopularRecipes(popularRecipeList: List<Recipe>) {

    Column(
        modifier = Modifier
            .padding(top = 32.dp)
    ) {
        Text(text = "Popular Recipes", fontWeight = FontWeight.Bold,
            fontSize = 16.sp)

        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ){

            items(popularRecipeList) {
                Box(
                    modifier = Modifier
                        .size(156.dp)
                        .padding(top = 16.dp)
                        .clip(RoundedCornerShape(16.dp)),

                ){

                    Image(
                        painter = painterResource(id = R.drawable.bg_image),
                        contentDescription = "",
                        modifier = Modifier

                    )
                }

            }
        }
    }

}

@Preview(apiLevel = 33)
@Composable
private fun HomeScreenPreview() {
    RecipeAppTheme {
        // A surface container using the 'background' color from the theme
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            HomeScreen(navController = rememberNavController())
        }
    }
}

