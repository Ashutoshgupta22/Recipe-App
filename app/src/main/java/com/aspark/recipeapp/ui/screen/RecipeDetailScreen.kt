package com.aspark.recipeapp.ui.screen

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.aspark.networking.model.Equipment
import com.aspark.networking.model.Ingredient
import com.aspark.networking.model.RecipeResponse
import com.aspark.recipeapp.MyResult
import com.aspark.recipeapp.ui.component.BoldTitle
import com.aspark.recipeapp.ui.component.ExpandableCard
import com.aspark.recipeapp.ui.component.MyAsyncImage
import com.aspark.recipeapp.ui.component.shimmerEffect
import com.aspark.recipeapp.ui.theme.AppOrange
import com.aspark.recipeapp.ui.theme.RecipeAppTheme
import com.aspark.recipeapp.viewmodel.RecipeDetailViewModel

@Composable
fun RecipeDetailScreen(
    id: Long, detailViewModel: RecipeDetailViewModel = viewModel()
) {
    LaunchedEffect(Unit) {
//        delay(7000)
        detailViewModel.getRecipeById(id)
    }

    when (val recipe = detailViewModel.recipe.collectAsState().value) {
        is MyResult.Success -> {
            Log.i("RecipeDetailScreen", "RecipeDetailScreen: Success")

            Content(recipe.data, detailViewModel)
            Log.i("RecipeDetail", "RecipeDetailScreen: ${recipe.data}")
        }

        is MyResult.Failure -> {
            ErrorScreen()
        }

        MyResult.Loading -> {
            Log.i("RecipeDetailScreen", "RecipeDetailScreen: Loading")
            Shimmer()
        }
    }
}

@Composable
fun Content(recipe: RecipeResponse, detailViewModel: RecipeDetailViewModel) {
    val context = LocalContext.current

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .verticalScroll(state = rememberScrollState(), enabled = true)
    ) {
        RecipeImage(recipe = recipe, onAddFav = {
            detailViewModel.addToFavorites(it)
            Toast.makeText(context, "Added to favorites", Toast.LENGTH_SHORT).show()
        }, onRemoveFav = { id ->
            detailViewModel.deleteFavoriteRecipe(id)
            Toast.makeText(context, "Removed from favorites", Toast.LENGTH_SHORT).show()
        })

        Column(
            modifier = Modifier.padding(16.dp),
        ) {
            Spacer(modifier = Modifier.height(16.dp))

            QuickCards(recipe)
            Spacer(modifier = Modifier.height(32.dp))

            Ingredients(recipe.extendedIngredients)
            Spacer(modifier = Modifier.height(32.dp))

            BoldTitle(title = "Instructions")

            Text(
                text = recipe.instructions, fontSize = 14.sp
            )
            Spacer(modifier = Modifier.height(32.dp))

            Equipments(recipe.equipment)
            Spacer(modifier = Modifier.height(32.dp))

            BoldTitle(title = "Quick Summary")

            Text(
                text = recipe.summary, fontSize = 14.sp
            )
        }

        ExpandableCard(title = "Nutrition", description = "Some Information")
        Spacer(modifier = Modifier.height(4.dp))
        ExpandableCard(title = "Bad for health nutrition", description = "Some Information")
        Spacer(modifier = Modifier.height(4.dp))

        ExpandableCard(title = "Good for health nutrition", description = "Some Information")
        Spacer(modifier = Modifier.height(16.dp))
    }
}

@Composable
fun Shimmer() {

    Box(modifier = Modifier.fillMaxSize()) {
        Column {
            MyAsyncImage(
                url = "", modifier = Modifier
                    .fillMaxWidth()
                    .height(300.dp)
                    .shimmerEffect()
            )

            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 32.dp)
            ) {
                repeat(3) {
                    OutlinedCard(
                        modifier = Modifier
                            .size(70.dp)
                            .weight(1f),
                        shape = RoundedCornerShape(12.dp),
                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .shimmerEffect()
                        )
                    }
                }
            }

            repeat(3) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(16.dp)
                        .padding(vertical = 16.dp)
                        .shimmerEffect()
                )
            }
        }
    }
}

@Composable
fun ErrorScreen() {

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp), contentAlignment = Alignment.Center
    ) {
        Text(text = "Oops! something went wrong!")
    }
}

@Composable
fun Equipments(equipmentList: List<Equipment>) {

    BoldTitle(title = "Equipments")
    LazyRow {

        items(equipmentList) { equipment ->

            Column(
                modifier = Modifier, horizontalAlignment = Alignment.CenterHorizontally
            ) {
                MyAsyncImage(
                    url = equipment.image, modifier = Modifier
                        .size(86.dp)
                        .clip(CircleShape)
                )

                Text(text = equipment.name, fontSize = 12.sp, color = Color.Black)
            }
        }

    }
}

@Composable
fun Ingredients(ingredientList: List<Ingredient>) {

    BoldTitle(title = "Ingredients")

    LazyRow(
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {

        items(ingredientList) { ingredient ->

            Column(
                modifier = Modifier, horizontalAlignment = Alignment.CenterHorizontally
            ) {
                MyAsyncImage(
                    url = ingredient.image, modifier = Modifier
                        .size(86.dp)
                        .clip(CircleShape)
                )

                Text(text = ingredient.name, fontSize = 12.sp, color = Color.Black)
            }
        }
    }
}

@Composable
fun QuickCards(recipe: RecipeResponse) {

    Row(
        horizontalArrangement = Arrangement.spacedBy(8.dp), modifier = Modifier.fillMaxWidth()
    ) {
        OutlinedCard(
            shape = RoundedCornerShape(12.dp), modifier = Modifier.weight(1f)
        ) {
            Column(
                modifier = Modifier
                    .padding(4.dp)
                    .fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(text = "Ready in", fontSize = 12.sp)
                Text(
                    text = "${recipe.readyInMinutes} min", fontSize = 16.sp, color = AppOrange,
                    textAlign = TextAlign.Center,
                )
            }
        }

        OutlinedCard(
            shape = RoundedCornerShape(12.dp), modifier = Modifier.weight(1f)
        ) {
            Column(
                modifier = Modifier
                    .padding(4.dp)
                    .fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(text = "Servings", fontSize = 12.sp)
                Text(
                    text = "${recipe.servings}", fontSize = 16.sp, color = AppOrange,
                )
            }
        }

        OutlinedCard(
            shape = RoundedCornerShape(12.dp), modifier = Modifier.weight(1f)
        ) {
            Column(
                modifier = Modifier
                    .padding(4.dp)
                    .fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Price/serving",
                    fontSize = 12.sp,
                )
                Text(
                    text = "${recipe.pricePerServing}",
                    fontSize = 16.sp,
                    color = AppOrange,
                )
            }
        }
    }
}

@Composable
fun RecipeImage(
    recipe: RecipeResponse, onAddFav: (RecipeResponse) -> Unit, onRemoveFav: (Long) -> Unit
) {

    var clicked by remember { mutableStateOf(false) }

    Box(
        modifier = Modifier
    ) {
        MyAsyncImage(
            url = recipe.image, modifier = Modifier
                .fillMaxWidth()
                .height(300.dp)
        )

        Text(
            text = recipe.title,
            fontWeight = FontWeight.Bold,
            color = Color.White,
            fontSize = 20.sp,
            modifier = Modifier
                .align(Alignment.BottomStart)
                .padding(start = 16.dp, bottom = 20.dp)
        )
        IconButton(onClick = {
            clicked = !clicked

            if (clicked) onAddFav(recipe) else onRemoveFav(recipe.id)
        },
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(end = 16.dp, top = 16.dp)
                .drawBehind {
                    drawCircle(Color.White, 44f)
                }) {
            Icon(
                imageVector = if (clicked) Icons.Default.Favorite
                else Icons.Outlined.FavoriteBorder,
                contentDescription = "",
                tint = AppOrange,
            )
        }
    }
}

@Preview(apiLevel = 33)
@Composable
fun RecipeDetailScreenPreview() {

    RecipeAppTheme {
        Surface(
            modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background
        ) {
            RecipeDetailScreen(id = 0)
        }
    }
}
