package com.aspark.recipeapp.ui.screen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.aspark.recipeapp.ui.component.MySearchBar

@Composable
fun SearchScreen(
    navController: NavController,
) {

    Column(
        modifier = Modifier
            .padding(top = 18.dp)
    ) {

        MySearchBar({

        }) {

        }
    }
}