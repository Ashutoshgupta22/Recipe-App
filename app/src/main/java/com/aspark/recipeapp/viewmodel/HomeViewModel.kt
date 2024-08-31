package com.aspark.recipeapp.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aspark.networking.ApiClient
import com.aspark.networking.model.RecipeResponse
import com.aspark.recipeapp.MyApplication
import com.aspark.recipeapp.UiState
import com.aspark.recipeapp.repository.RecipeRepository
import com.aspark.recipeapp.room.RecipeDatabase
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class HomeViewModel() : ViewModel() {

    private val database = RecipeDatabase.getDatabase(MyApplication.applicationContext())
    private val repository: RecipeRepository = RecipeRepository(
        ApiClient.apiService, database.recipeDao(), database.ingredientDao(),
        database.equipmentDao()
    )

    init {
//        getRandomRecipes()
    }

    val randomRecipes: StateFlow<UiState<List<RecipeResponse>>> = repository.getRandomRecipes()
        .stateIn(
            viewModelScope,
            SharingStarted.Lazily,
            UiState.Loading
        )

    private var isDataLoaded = false;

    fun getRandomRecipes() {

        if (isDataLoaded) return

        viewModelScope.launch {
            repository.getRandomRecipes().collect { result ->

                when (result) {
                    is UiState.Success -> {
                        Log.i("HomeViewModel", "getRandomRecipes: Success")
                        isDataLoaded = true
                    }

                    is UiState.Error -> {
                        Log.e("HomeViewModel", "getRandomRecipes: Failed", result.exception)
                    }

                    is UiState.Loading -> {
                        Log.d("HomeViewModel", "getRandomRecipes: Loading")
                    }
                    else -> {}
                }
            }
        }
    }
}
