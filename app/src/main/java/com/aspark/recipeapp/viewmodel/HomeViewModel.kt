package com.aspark.recipeapp.viewmodel

import android.util.Log
import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aspark.networking.ApiClient
import com.aspark.networking.model.RecipeResponse
import com.aspark.recipeapp.MyApplication
import com.aspark.recipeapp.MyResult
import com.aspark.recipeapp.repository.RecipeRepository
import com.aspark.recipeapp.room.RecipeDao
import com.aspark.recipeapp.room.RecipeDatabase
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlin.math.log

class HomeViewModel() : ViewModel() {

    private val database = RecipeDatabase.getDatabase(MyApplication.applicationContext())
    private val repository: RecipeRepository = RecipeRepository(
        ApiClient.apiService, database.recipeDao(), database.ingredientDao(),
        database.equipmentDao()
    )

    val randomRecipes: StateFlow<MyResult<List<RecipeResponse>>> = repository.getRandomRecipes()
        .stateIn(
            viewModelScope,
            SharingStarted.Lazily,
            MyResult.Loading
        )

    private var isDataLoaded = false;

    fun getRandomRecipes() {

        if (isDataLoaded) return

        viewModelScope.launch {
            repository.getRandomRecipes().collect { result ->

                when (result) {
                    is MyResult.Success -> {
                        Log.i("HomeViewModel", "getRandomRecipes: Success")
                        isDataLoaded = true
                    }

                    is MyResult.Failure -> {
                        Log.e("HomeViewModel", "getRandomRecipes: Failed", result.exception)
                    }

                    is MyResult.Loading -> {
                        Log.d("HomeViewModel", "getRandomRecipes: Loading")
                    }
                }
            }
        }
    }
}
