package com.aspark.recipeapp.viewmodel

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aspark.networking.ApiClient
import com.aspark.recipeapp.MyApplication
import com.aspark.recipeapp.UiState
import com.aspark.recipeapp.room.RecipeEntity
import com.aspark.recipeapp.repository.RecipeRepository
import com.aspark.recipeapp.room.RecipeDatabase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class FavoriteViewModel: ViewModel() {

    private val database = RecipeDatabase.getDatabase(MyApplication.applicationContext())
    private val repository: RecipeRepository = RecipeRepository(
        ApiClient.apiService,  database.recipeDao(), database.ingredientDao(),
        database.equipmentDao()
    )

    private var _favRecipes = MutableStateFlow<UiState<List<RecipeEntity>>>(UiState.Loading)
    val favRecipes = _favRecipes.asStateFlow()


    fun getFavRecipes() {
        viewModelScope.launch {
            repository.getFavoriteRecipes().collect {
                _favRecipes.value = it
            }
        }
    }

    fun addToFavorites(recipe: RecipeEntity) = viewModelScope.launch {
        repository.addToFavorites(recipe)
    }

}