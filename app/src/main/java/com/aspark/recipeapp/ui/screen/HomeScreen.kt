package com.aspark.recipeapp.ui.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SearchBar
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import com.aspark.networking.RecipeResponse
import com.aspark.recipeapp.ui.Screen
import com.aspark.recipeapp.ui.component.BottomNavigationBar
import com.aspark.recipeapp.ui.component.rememberMyAsyncPainter
import com.aspark.recipeapp.ui.theme.RecipeAppTheme
import com.aspark.recipeapp.viewmodel.HomeViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    navController: NavController,
    homeViewModel: HomeViewModel = viewModel()
) {

    LaunchedEffect(Unit) {
        homeViewModel.getRandomRecipes()
    }

    Column(
        modifier = Modifier.padding(16.dp)
    ) {

        Text(text = "\uD83D\uDC4B Hey Ashu", fontWeight = FontWeight.Medium)
        Text(text = "Discover tasty and healthy recipes", fontSize = 12.sp)

        SearchBar(
            query = "",
            onQueryChange = {

            },
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
            shape = RoundedCornerShape(12.dp),
            modifier = Modifier
                .fillMaxWidth()

        ) {

        }

        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            item {
                PopularRecipes(homeViewModel.randomRecipes.toList())

                Spacer(modifier = Modifier.height(32.dp))

                Text(
                    text = "All recipes", fontWeight = FontWeight.Bold,
                    fontSize = 16.sp
                )
            }

            itemsIndexed(homeViewModel.randomRecipes.toList()) { index, recipe ->
                AllRecipes(recipe) { id ->
                    navController.navigate(Screen.RecipeDetail.createRoute(id))
                }

                if (index == homeViewModel.randomRecipes.lastIndex)
                    Spacer(modifier = Modifier.height(80.dp))
            }
        }

    }
}

@Composable
fun AllRecipes(recipe: RecipeResponse, cardClicked: (Long)-> Unit) {

    OutlinedCard(
        onClick = { cardClicked(recipe.id) },
        shape = RoundedCornerShape(12.dp),
        modifier = Modifier
            .fillMaxWidth()
            .height(100.dp)
    ) {
        Row {
            Image(
                painter = rememberMyAsyncPainter(recipe.image),
                contentDescription = "",
                modifier = Modifier
                    .fillMaxHeight()
                    .width(100.dp),
                contentScale = ContentScale.Crop,
            )

            Column(
                modifier = Modifier
                    .padding(horizontal = 12.dp)
                    .align(Alignment.CenterVertically)
            ) {
                Text(
                    text = recipe.title, fontSize = 14.sp,
                    color = Color.Black, fontWeight = FontWeight.Medium,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )

                Text(
                    text = "Ready in 25 min", fontSize = 14.sp,
                    color = Color.Gray, fontWeight = FontWeight.Medium,
                    maxLines = 1,
                )
            }
        }
    }
}


@Composable
fun PopularRecipes(popularRecipes: List<RecipeResponse>) {

    Column(
        modifier = Modifier
            .padding(top = 32.dp)
    ) {
        Text(
            text = "Popular Recipes", fontWeight = FontWeight.Bold,
            fontSize = 16.sp
        )

        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {

            items(popularRecipes) { recipe ->
                Box(
                    modifier = Modifier
                        .size(156.dp)
                        .padding(top = 16.dp)
                        .clip(RoundedCornerShape(16.dp)),

                    ) {
                    Image(
                        painter = rememberMyAsyncPainter(recipe.image),
                        contentDescription = "",
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop,
                    )

                    Column(
                        modifier = Modifier
                            .align(Alignment.BottomStart)
                            .padding(start = 8.dp)
                    ) {

                        Text(
                            text = recipe.title, fontSize = 14.sp,
                            color = Color.White, fontWeight = FontWeight.Medium,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )

                        Text(
                            text = "Ready in 25 min", fontSize = 12.sp,
                            color = Color.Gray, fontWeight = FontWeight.Medium,
                            maxLines = 1,
                        )
                    }


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
            HomeScreen(rememberNavController())
        }
    }
}

