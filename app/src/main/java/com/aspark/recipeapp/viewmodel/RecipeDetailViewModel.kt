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
import com.aspark.recipeapp.room.RecipeEntity
import com.aspark.recipeapp.repository.RecipeRepository
import com.aspark.recipeapp.room.RecipeDatabase
import kotlinx.coroutines.launch

class RecipeDetailViewModel(): ViewModel() {

    private val database = RecipeDatabase.getDatabase(MyApplication.applicationContext())
    private val repository: RecipeRepository = RecipeRepository(
        ApiClient.apiService,  database.recipeDao(), database.ingredientDao(),
        database.equipmentDao()
    )

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

    fun addToFavorites(recipe: RecipeEntity) = viewModelScope.launch {
        repository.addToFavorites(recipe)
    }
}