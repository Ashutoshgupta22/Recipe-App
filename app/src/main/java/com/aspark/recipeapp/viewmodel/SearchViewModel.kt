package com.aspark.recipeapp.viewmodel

import android.util.Log
import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aspark.networking.ApiClient
import com.aspark.networking.model.SearchSuggestionResponse
import com.aspark.recipeapp.MyApplication
import com.aspark.recipeapp.UiState
import com.aspark.recipeapp.repository.RecipeRepository
import com.aspark.recipeapp.room.RecipeDatabase
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class SearchViewModel : ViewModel() {
    private val database = RecipeDatabase.getDatabase(MyApplication.applicationContext())
    private val repository: RecipeRepository = RecipeRepository(
        ApiClient.apiService,  database.recipeDao(), database.ingredientDao(),
        database.equipmentDao()
    )

    private var _suggestions = MutableStateFlow<UiState<List<SearchSuggestionResponse>>>(
        UiState.Idle
    )
    val suggestions: StateFlow<UiState<List<SearchSuggestionResponse>>> = _suggestions

    private var searchJob: Job? = null

    fun getSearchSuggestions(query: String) {

        _suggestions.value = UiState.Loading

        searchJob?.cancel(message = "Job cancelled")
        searchJob = viewModelScope.launch {

            delay(700)
             repository.getSearchSuggestions(query).collect{
                _suggestions.value = it
            }
        }
    }

    fun clearSuggestions() {
        _suggestions.value = UiState.Idle
    }
}