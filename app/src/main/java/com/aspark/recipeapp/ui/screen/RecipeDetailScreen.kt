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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
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
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.aspark.networking.model.Equipment
import com.aspark.networking.model.Ingredient
import com.aspark.networking.model.RecipeResponse
import com.aspark.recipeapp.MyResult
import com.aspark.recipeapp.ui.component.AnnotatedText
import com.aspark.recipeapp.ui.component.BoldTitle
import com.aspark.recipeapp.ui.component.BulletText
import com.aspark.recipeapp.ui.component.ExpandableCard
import com.aspark.recipeapp.ui.component.MyAsyncImage
import com.aspark.recipeapp.ui.component.shimmerEffect
import com.aspark.recipeapp.ui.theme.AppOrange
import com.aspark.recipeapp.ui.theme.RecipeAppTheme
import com.aspark.recipeapp.utility.parseHtml
import com.aspark.recipeapp.utility.parseSummary
import com.aspark.recipeapp.viewmodel.RecipeDetailViewModel

@Composable
fun RecipeDetailScreen(
    id: Long, detailViewModel: RecipeDetailViewModel = viewModel()
) {
    LaunchedEffect(id) {
//        delay(7000)
        detailViewModel.getRecipeById(id)
        detailViewModel.checkIfFavorite(id)
    }

    when (val recipe = detailViewModel.recipe.collectAsState().value) {
        is MyResult.Success -> {
            Log.i("RecipeDetailScreen", "RecipeDetailScreen: Success")

            Content(recipe.data, detailViewModel)
            Log.i("RecipeDetail", "RecipeDetailScreen: ${recipe.data.summary}")
        }

        is MyResult.Failure -> {
            ErrorScreen()
        }

        MyResult.Loading -> {
            Log.i("RecipeDetailScreen", "RecipeDetailScreen: Loading")
            ShimmerScreen()
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
        RecipeImage(recipe, detailViewModel)

        Column(
            modifier = Modifier.padding(16.dp),
        ) {
            Spacer(modifier = Modifier.height(16.dp))

            QuickCards(recipe)
            Spacer(modifier = Modifier.height(32.dp))

//            Ingredients(recipe.extendedIngredients)
            BoldTitle(title = "Ingredients")

            IngredientsList(recipe.extendedIngredients)
            Spacer(modifier = Modifier.height(32.dp))

            BoldTitle(title = "Instructions")

            Instructions(recipe.instructions)

            Spacer(modifier = Modifier.height(32.dp))

//            Equipments(recipe.equipment)
//            Spacer(modifier = Modifier.height(32.dp))

            BoldTitle(title = "Quick Summary")
            QuickSummary(recipe.summary)
        }

//        ExpandableCard(title = "Nutrition", description = "Some Information")
//        Spacer(modifier = Modifier.height(4.dp))
//        ExpandableCard(title = "Bad for health nutrition", description = "Some Information")
//        Spacer(modifier = Modifier.height(4.dp))
//
//        ExpandableCard(title = "Good for health nutrition", description = "Some Information")
//        Spacer(modifier = Modifier.height(16.dp))
    }
}

@Composable
fun IngredientsList(extendedIngredients: List<Ingredient>) {

    val midPoint = (extendedIngredients.size + 1 )/ 2

    Row(
        modifier = Modifier
            .padding(top = 8.dp)
    ) {
        Column(
            modifier = Modifier
                .weight(1f)
        ) {
            extendedIngredients.take(midPoint).forEach { ingredient ->
                BulletText(text = ingredient.name)
            }
        }

        Spacer(modifier = Modifier.width(8.dp))

        Column(
            modifier = Modifier
                .weight(1f)
        ) {
            extendedIngredients.drop(midPoint).forEach { ingredient ->
                BulletText(text = ingredient.name)
            }
        }
    }
}

@Composable
fun QuickSummary(unParsedSummary: String) {
    val parsedInfo = parseSummary(unParsedSummary)

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 8.dp, bottom = 16.dp)
    ) {
        parsedInfo["protein"]?.let {
           AnnotatedText(boldText = "Protein:", normalText = it)
        }
        parsedInfo["fat"]?.let {
            AnnotatedText(boldText = "Fat:", normalText = it)
        }
        parsedInfo["calories"]?.let {
            AnnotatedText(boldText = "Calories:", normalText = it)
        }
        parsedInfo["nutritionCoverage"]?.let {
            AnnotatedText(boldText = "Nutrition Coverage:", normalText = it)
        }
        parsedInfo["score"]?.let {
            AnnotatedText(boldText = "Score:", normalText = it)
        }
    }
}

@Composable
fun Instructions(unParsedInstructions: String) {

    val instructions = parseHtml(unParsedInstructions)

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 8.dp)
    ) {
        instructions.forEachIndexed { index, instruction ->
            Text(
                text = buildAnnotatedString {
                    withStyle(
                        style = SpanStyle(fontWeight = FontWeight.Bold)
                    ) {
                        append("${index + 1}. ")
                    }
                    append(instruction)
                },
                fontSize = 16.sp,
                color = Color.Black
            )
        }
    }
}

@Composable
fun ShimmerScreen() {

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
        Text(text = "Oops! something went wrong")
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
    recipe: RecipeResponse, viewModel: RecipeDetailViewModel
) {
    val context = LocalContext.current

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

        val isFavorite = viewModel.isFavorite.collectAsState().value

        IconButton(
            onClick = {
                if (isFavorite) {
                    viewModel.deleteFavoriteRecipe(recipe.id)
                    Toast.makeText(context, "Removed from favorites", Toast.LENGTH_SHORT).show()
                } else {
                    viewModel.addToFavorites(recipe)
                    Toast.makeText(context, "Added to favorites", Toast.LENGTH_SHORT).show()
                }
            },
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(end = 16.dp, top = 16.dp)
                .drawBehind {
                    drawCircle(Color.White, 44f)
                }) {
            Icon(
                imageVector = if (isFavorite) Icons.Default.Favorite
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
