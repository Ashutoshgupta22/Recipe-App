package com.aspark.recipeapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aspark.networking.ApiClient
import com.aspark.networking.model.RecipeResponse
import com.aspark.recipeapp.MyApplication
import com.aspark.recipeapp.MyResult
import com.aspark.recipeapp.repository.RecipeRepository
import com.aspark.recipeapp.room.RecipeDatabase
import com.aspark.recipeapp.room.toEntity
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

@OptIn(ExperimentalCoroutinesApi::class)
class RecipeDetailViewModel() : ViewModel() {

    private val database = RecipeDatabase.getDatabase(MyApplication.applicationContext())
    private val repository: RecipeRepository = RecipeRepository(
        ApiClient.apiService, database.recipeDao(), database.ingredientDao(),
        database.equipmentDao()
    )

    private val _recipeId = MutableStateFlow<Long?>(null)

    private val _isFavorite = MutableStateFlow(false)
    val isFavorite: StateFlow<Boolean> = _isFavorite

    val recipe: StateFlow<MyResult<RecipeResponse>> = _recipeId
        .filterNotNull()
        .flatMapLatest { id ->
            repository.getRecipeById(id)
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.Lazily,
            initialValue = MyResult.Loading
        )

    fun getRecipeById(id: Long) {
        _recipeId.value = id
    }

    fun addToFavorites(recipe: RecipeResponse) = viewModelScope.launch {
        _isFavorite.value = true

        val entity = recipe.toEntity()
        entity.isFavorite = true
        repository.addToFavorites(entity)
    }

    fun deleteFavoriteRecipe(recipeId: Long) = viewModelScope.launch {
        _isFavorite.value = false
        repository.deleteFavoriteRecipe(recipeId)
    }

     fun checkIfFavorite(recipeId: Long) {
        viewModelScope.launch {
            _isFavorite.value = repository.checkIfFavorite(recipeId)
        }
    }
}