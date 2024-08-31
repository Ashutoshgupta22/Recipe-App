package com.aspark.recipeapp.ui.screen

import android.util.Log
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.aspark.networking.model.SearchSuggestionResponse
import com.aspark.recipeapp.UiState
import com.aspark.recipeapp.ui.Screen
import com.aspark.recipeapp.ui.component.MySearchBar
import com.aspark.recipeapp.ui.theme.RecipeAppTheme
import com.aspark.recipeapp.viewmodel.SearchViewModel

@Composable
fun SearchScreen(
    navController: NavController,
    searchViewModel: SearchViewModel = viewModel()
) {
    val keyboardController = LocalSoftwareKeyboardController.current

    BackHandler(enabled = true) {
        Log.i("BackHandler", "SearchScreen: back pressed")
        navController.popBackStack()
    }

    Column(
        modifier = Modifier
    ) {
        MySearchBar(
            onBack = {
                keyboardController?.hide()
                navController.popBackStack()
            },
            onClear = { searchViewModel.clearSuggestions() },
            onSearch = { query ->
                searchViewModel.getSearchSuggestions(query)
            }
        ) {
            SuggestionList(searchViewModel.suggestions.collectAsState().value) { recipeId ->
                navController.navigate(Screen.RecipeDetail.createRoute(recipeId))
            }
        }
    }
}

@Composable
fun SuggestionList(
    uiState: UiState<List<SearchSuggestionResponse>>,
    onClick: (Long) -> Unit
) {

    when(uiState) {
        is UiState.Success -> {
            SuggestionListContent(uiState.data, onClick)
        }
        is UiState.Error -> {
            ErrorScreen("No match found")
        }
        is UiState.Loading -> {
            Box(modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        }
        else -> {}
    }
}

@Composable
fun SuggestionListContent(suggestions: List<SearchSuggestionResponse>, onClick: (Long) -> Unit) {

    LazyColumn(
        contentPadding = PaddingValues(vertical = 16.dp, horizontal = 16.dp),
        modifier = Modifier
            .fillMaxWidth()
    ) {
        items(suggestions) { item: SearchSuggestionResponse ->
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onClick(item.id) }
            ) {
                Text(
                    text = item.title,
                    color = MaterialTheme.colorScheme.onSurface,
                    fontSize = 18.sp,
                    modifier = Modifier
                        .padding(vertical = 8.dp, horizontal = 8.dp)
                )
            }
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