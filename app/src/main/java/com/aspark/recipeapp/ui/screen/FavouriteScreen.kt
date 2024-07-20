package com.aspark.recipeapp.ui.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModel
import androidx.navigation.NavController
import com.aspark.recipeapp.viewmodel.FavoriteViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import com.aspark.recipeapp.ui.component.MyAsyncImage

@Composable
fun FavouriteScreen(navController: NavController, viewModel: FavoriteViewModel = viewModel()) {

    LaunchedEffect(Unit) {
        viewModel.getFavRecipes()
    }

    LazyColumn(
        modifier = Modifier
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {

        items(viewModel.favRecipes) { recipe ->

            OutlinedCard(
                onClick = {  },
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
    }


}
