package com.aspark.recipeapp.ui.screen

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.aspark.networking.model.SearchSuggestionResponse
import com.aspark.recipeapp.ui.component.MySearchBar
import com.aspark.recipeapp.ui.theme.RecipeAppTheme
import com.aspark.recipeapp.viewmodel.SearchViewModel

@Composable
fun SearchScreen(
    navController: NavController,
    searchViewModel: SearchViewModel = viewModel()
) {

    BackHandler {
        navController.popBackStack()
    }

    Column(
        modifier = Modifier
    ) {

        MySearchBar(
            isSearchScreen = true,
            onBack = { navController.popBackStack() },
            onActiveChange = {},
            onSearch = { query ->
                searchViewModel.getSearchSuggestions(query)
            }
        ) {
            SuggestionList(searchViewModel.suggestions.toList())
        }
    }
}

@Composable
fun SuggestionList(suggestions: List<SearchSuggestionResponse>) {

    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(4.dp),
        contentPadding = PaddingValues(vertical = 16.dp, horizontal = 16.dp)
    ){
        items(suggestions) { item: SearchSuggestionResponse ->

            Text(text = item.title, color = Color.Black)
        }
    }

}

@Preview(apiLevel = 33)
@Composable
fun SearchScreenPreview() {
    RecipeAppTheme {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            SearchScreen(rememberNavController())
        }
    }
}