package com.aspark.recipeapp.viewmodel

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aspark.networking.ApiClient
import com.aspark.recipeapp.MyApplication
import com.aspark.recipeapp.model.RecipeEntity
import com.aspark.recipeapp.repository.RecipeRepository
import com.aspark.recipeapp.room.RecipeDatabase
import kotlinx.coroutines.launch

class FavoriteViewModel: ViewModel() {

    private val database = RecipeDatabase.getDatabase(MyApplication.applicationContext())
    private val repository: RecipeRepository = RecipeRepository(
        ApiClient.apiService,  database.recipeDao(), database.ingredientDao(),
        database.equipmentDao()
    )

    var favRecipes = mutableStateListOf<RecipeEntity>()
        private set

    fun getFavRecipes() {
        viewModelScope.launch {
            favRecipes.clear()
            repository.getFavoriteRecipes()?.let { favRecipes.addAll(it) }
        }
    }

    fun addToFavorites(recipe: RecipeEntity) = viewModelScope.launch {
        repository.addToFavorites(recipe)
    }

}