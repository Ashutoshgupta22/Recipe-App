package com.aspark.recipeapp.viewmodel

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aspark.networking.ApiClient
import com.aspark.networking.model.RecipeResponse
import com.aspark.recipeapp.MyApplication
import com.aspark.recipeapp.model.Recipe
import com.aspark.recipeapp.repository.RecipeRepository
import com.aspark.recipeapp.room.RecipeDatabase
import kotlinx.coroutines.launch

class RecipeDetailViewModel(): ViewModel() {

    private val recipeDao = RecipeDatabase.getDatabase(MyApplication.applicationContext()).recipeDao()
    private val repository: RecipeRepository = RecipeRepository(ApiClient.apiService, recipeDao )

    var recipe by mutableStateOf(RecipeResponse())
        private set

    fun getRecipeById(id: Long) {
        viewModelScope.launch {
                val response = repository.getRecipeById(id)
            if (response != null) {
                recipe = response
            }
            else Log.e("TAG", "getRecipeById: Body Null" )
        }
    }

    fun addToFavorites(recipe: Recipe) = viewModelScope.launch {
        repository.addToFavorites(recipe)
    }
}