package com.aspark.recipeapp.viewmodel

import android.util.Log
import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aspark.networking.ApiClient
import com.aspark.networking.RecipeResponse
import com.aspark.recipeapp.repository.RecipeRepository
import kotlinx.coroutines.launch

class HomeViewModel() : ViewModel() {
    private val repository: RecipeRepository = RecipeRepository(ApiClient.apiService, null )

    var randomRecipes = mutableStateListOf<RecipeResponse>()
        private set
    var searchRecipes = mutableStateListOf<RecipeResponse>()
        private set

    fun getRandomRecipes() {
        viewModelScope.launch {
            val recipes = repository.getRandomRecipes()
            if (recipes != null) {
                randomRecipes.clear()
                randomRecipes.addAll(recipes)
                Log.i("TAG", "getRandomRecipes: ${randomRecipes.toList()}")
            }
            else Log.e("TAG", "getRandomRecipes: Empty Body" )
        }
    }

    fun searchRecipes(query: String) {
        viewModelScope.launch {
            val response = repository.searchRecipes(query)
            if (response.isSuccessful) {
                searchRecipes.clear()
                response.body()?.let { searchRecipes.addAll(it) }
            }
        }
    }
}