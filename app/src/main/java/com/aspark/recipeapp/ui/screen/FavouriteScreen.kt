package com.aspark.recipeapp.ui.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.aspark.recipeapp.viewmodel.FavoriteViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import com.aspark.recipeapp.UiState
import com.aspark.recipeapp.room.RecipeEntity
import com.aspark.recipeapp.ui.component.MyAsyncImage

@Composable
fun FavouriteScreen(
    navController: NavController,
    innerPadding: PaddingValues,
    viewModel: FavoriteViewModel = viewModel()
) {
    LaunchedEffect(Unit) {
        viewModel.getFavRecipes()
    }
        when (val favRecipesState = viewModel.favRecipes.collectAsState().value) {
            is UiState.Success -> {
                if (favRecipesState.data.isNotEmpty()) {
                    FavoriteScreenContent(
                        data = favRecipesState.data,
                        innerPadding = innerPadding,
                        onItemClick = { recipeId ->
                            navController.navigate("recipeDetail/$recipeId")
                        }
                    )
                } else EmptyScreen()
            }
            is UiState.Error -> {
                ErrorScreen()
            }
            is UiState.Loading -> {
                LoadingScreen()
            }
            else -> {}
        }
}

@Composable
fun EmptyScreen() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text(text = "No favorite recipes added")
    }
}

@Composable
private fun FavoriteScreenContent(
    data: List<RecipeEntity>,
    innerPadding: PaddingValues,
    onItemClick: (Long) -> Unit
) {
    LazyColumn(
        modifier = Modifier
            .padding(innerPadding)
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
        contentPadding = PaddingValues(vertical = 16.dp)
    ) {
        items(data) { recipe ->
            OutlinedCard(
                onClick = { onItemClick(recipe.id) },
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(100.dp)
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
                            color = MaterialTheme.colorScheme.onSurface,
                            fontWeight = FontWeight.Medium,
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
    }
}
