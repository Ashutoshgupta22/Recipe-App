package com.aspark.recipeapp.viewmodel

import android.util.Log
import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aspark.networking.ApiClient
import com.aspark.networking.model.RecipeResponse
import com.aspark.recipeapp.repository.RecipeRepository
import kotlinx.coroutines.launch

class HomeViewModel() : ViewModel() {
    private val repository: RecipeRepository = RecipeRepository(ApiClient.apiService, null )

    var randomRecipes = mutableStateListOf<RecipeResponse>()
        private set

    private var isDataLoaded = false;

    fun getRandomRecipes() {

        if (isDataLoaded) return

        viewModelScope.launch {
            val recipes = repository.getRandomRecipes()
            if (recipes != null) {
                randomRecipes.clear()
                randomRecipes.addAll(recipes)
                isDataLoaded = true
                Log.i("TAG", "getRandomRecipes: ${randomRecipes.toList()}")
            }
            else Log.e("TAG", "getRandomRecipes: Empty Body" )
        }
    }
}