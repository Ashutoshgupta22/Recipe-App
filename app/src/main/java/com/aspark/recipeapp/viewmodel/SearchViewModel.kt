package com.aspark.recipeapp.viewmodel

import android.util.Log
import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aspark.networking.ApiClient
import com.aspark.networking.model.SearchSuggestionResponse
import com.aspark.recipeapp.repository.RecipeRepository
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class SearchViewModel : ViewModel() {
    private val repository: RecipeRepository = RecipeRepository(ApiClient.apiService, null)

    var suggestions = mutableStateListOf<SearchSuggestionResponse>()
        private set

    private var searchJob: Job? = null

    fun getSearchSuggestions(query: String) {

        searchJob?.cancel(message = "Job cancelled")
        searchJob = viewModelScope.launch {

            delay(700)
            val response = repository.getSearchSuggestions(query)
            suggestions.clear()

            response?.let {
                suggestions.addAll(it)
            } ?: Log.e("SearchViewModel", "getSearchSuggestions: Empty Body")
        }
    }

    fun searchRecipe(query: String) {


    }
}