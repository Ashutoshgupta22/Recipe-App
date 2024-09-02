package com.aspark.recipeapp.ui.screen

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.aspark.networking.model.RecipeResponse
import com.aspark.recipeapp.UiState
import com.aspark.recipeapp.ui.Screen
import com.aspark.recipeapp.ui.component.MyAsyncImage
import com.aspark.recipeapp.ui.component.shimmerEffect
import com.aspark.recipeapp.ui.theme.RecipeAppTheme
import com.aspark.recipeapp.viewmodel.HomeViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    navController: NavController,
    innerPadding: PaddingValues,
    homeViewModel: HomeViewModel = viewModel()
) {
//    LaunchedEffect(Unit) {
//        homeViewModel.getRandomRecipes()
//    }
    Column(
        modifier = Modifier
            .padding(innerPadding)
    ) {
        Column(
            modifier = Modifier
                .padding(start = 16.dp, end = 16.dp, top = 16.dp, bottom = 8.dp)
        ) {
            Text(text = "\uD83D\uDC4B Hey!", fontWeight = FontWeight.Medium)
            Text(text = "Discover tasty and healthy recipes", fontSize = 12.sp)

            Spacer(modifier = Modifier.height(8.dp))
            SearchBarDuplicate {
                navController.navigate(Screen.Search.route)
            }
        }

        val recipeList by homeViewModel.randomRecipes.collectAsState()

        when (recipeList) {
            is UiState.Success -> {
                val data = (recipeList as UiState.Success<List<RecipeResponse>>).data

                HomeScreenContent(
                    recipeList = data,
                    onClick = { recipeId ->
                        navController.navigate(Screen.RecipeDetail.createRoute(recipeId))
                    }
                )
            }

            is UiState.Error -> {
                Log.e(
                    "HomeScreen", "HomeScreen: RandomRecipes Failed",
                    (recipeList as UiState.Error).exception
                )
            }

            is UiState.Loading -> {
                LoadingScreen()
            }
            else -> {
                Log.i("HomeScreen", "HomeScreen: Idle")
            }
        }
    }
}


@Composable
fun SearchBarDuplicate(onClick: () -> Unit) {
    Card(
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth()
            .height(60.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.secondaryContainer
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Default.Search,
                contentDescription = "search",
                tint = Color.Black
            )
            Spacer(modifier = Modifier.width(12.dp))
            Text(text = "Search any recipe")
        }
    }
}

@Composable
fun AllRecipesItem(recipe: RecipeResponse, cardClicked: (Long) -> Unit) {
    OutlinedCard(
        onClick = { cardClicked(recipe.id) },
        shape = RoundedCornerShape(12.dp),
        modifier = Modifier
            .fillMaxWidth()
            .height(100.dp)
            .padding(horizontal = 16.dp)
    ) {
        Row {
            MyAsyncImage(
                url = recipe.image,
                modifier = Modifier
                    .width(100.dp)
                    .fillMaxHeight()
            )

            Column(
                modifier = Modifier
                    .padding(horizontal = 12.dp)
                    .align(Alignment.CenterVertically)
            ) {
                Text(
                    text = recipe.title, fontSize = 14.sp,
                    color = MaterialTheme.colorScheme.onSurface, fontWeight = FontWeight.Medium,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                )

                Text(
                    text = "Ready in ${recipe.readyInMinutes} min", fontSize = 14.sp,
                    color = Color.Gray, fontWeight = FontWeight.Medium,
                    maxLines = 1,
                )
            }
        }
    }
}

@Composable
fun PopularRecipes(popularRecipes: List<RecipeResponse>, onClick: (Long) -> Unit) {

    Column(
        modifier = Modifier
            .padding(top = 16.dp)
    ) {
        Text(
            text = "Popular Recipes", fontWeight = FontWeight.Bold,
            fontSize = 16.sp,
            modifier = Modifier
                .padding(start = 16.dp)
        )

        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            contentPadding = PaddingValues(horizontal = 16.dp)
        ) {
            itemsIndexed(popularRecipes) { index, recipe ->
                PopularRecipeItem(recipe) { recipeId -> onClick(recipeId) }
            }
        }
    }
}

@Composable
fun HomeScreenContent(
    recipeList: List<RecipeResponse>,
    onClick: (Long) -> Unit
    ) {
    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(12.dp),
        contentPadding = PaddingValues(bottom = 16.dp)
    ) {
        item {
            PopularRecipes(recipeList.take(10)) { recipeId -> onClick(recipeId) }

            Spacer(modifier = Modifier.height(32.dp))

            Text(
                text = "All recipes", fontWeight = FontWeight.Bold,
                fontSize = 16.sp,
                modifier = Modifier
                    .padding(start = 16.dp)
            )
        }

        itemsIndexed(recipeList.drop(10)) { index, recipe ->
            AllRecipesItem(recipe) { id -> onClick(id)}
        }
    }
}

    @Composable
    fun PopularRecipeItem(recipe: RecipeResponse, onClick: (Long) -> Unit) {
        Box(
            modifier = Modifier
                .size(156.dp)
                .padding(top = 16.dp)
                .clip(RoundedCornerShape(16.dp))
                .clickable { onClick(recipe.id) },

            ) {

            MyAsyncImage(
                url = recipe.image,
                modifier = Modifier.fillMaxSize()
            )

            //to add black gradient tint
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        Brush.verticalGradient(
                            colors = listOf(
                                Color.Transparent,
                                Color.Black.copy(alpha = 0.7f)
                            ),
                            startY = 0f,
                            endY = Float.POSITIVE_INFINITY
                        )
                    )
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
                    text = "Ready in ${recipe.readyInMinutes} min", fontSize = 12.sp,
                    color = Color.LightGray, fontWeight = FontWeight.Medium,
                    maxLines = 1,
                )
            }
        }
    }

    @Composable
    fun LoadingScreen() {
        Log.i("HomeScreen", "LoadingScreen: called")
        Column(
            modifier = Modifier
                .padding(16.dp)
        ) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(12.dp),
            ) {
                repeat(3) {
                    Box(
                        modifier = Modifier
                            .size(156.dp)
                            .padding(top = 16.dp)
                            .clip(RoundedCornerShape(16.dp))
                            .shimmerEffect()
                    ) {}
                }
            }

            Spacer(modifier = Modifier.height(32.dp))
                repeat(5) {
                    Row(
                        modifier = Modifier
                            .padding(8.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(100.dp)
                                .clip(RoundedCornerShape(16.dp))
                                .shimmerEffect()
                        )
                        Spacer(modifier = Modifier.width(12.dp))
                        Column(
                            modifier = Modifier
                                .height(100.dp),
                            verticalArrangement = Arrangement.Center
                        ) {

                            Box(modifier = Modifier
                                .height(14.dp)
                                .width(100.dp)
                                .clip(RoundedCornerShape(16.dp))
                                .shimmerEffect()
                            )
                            Box(modifier = Modifier
                                .padding(top = 8.dp)
                                .height(14.dp)
                                .width(60.dp)
                                .clip(RoundedCornerShape(16.dp))
                                .shimmerEffect()
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
//                HomeScreen(rememberNavController())
                LoadingScreen()
            }
        }
    }

