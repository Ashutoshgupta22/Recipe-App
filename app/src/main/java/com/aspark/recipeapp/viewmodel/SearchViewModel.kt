package com.aspark.recipeapp.viewmodel

import android.util.Log
import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aspark.networking.ApiClient
import com.aspark.networking.model.SearchSuggestionResponse
import com.aspark.recipeapp.repository.RecipeRepository
import kotlinx.coroutines.launch

class SearchViewModel: ViewModel() {
    private val repository: RecipeRepository = RecipeRepository(ApiClient.apiService, null)

    var suggestions = mutableStateListOf<SearchSuggestionResponse>()
        private set

    fun getSearchSuggestions(query: String) {
        viewModelScope.launch {
            val response = repository.getSearchSuggestions(query)
            if (response != null) {
                suggestions.clear()
                suggestions.addAll(response)
            }
            else Log.e("SearchViewModel", "getSearchSuggestions: Empty Body", )
        }
    }

    fun searchRecipe(query: String) {


    }
}