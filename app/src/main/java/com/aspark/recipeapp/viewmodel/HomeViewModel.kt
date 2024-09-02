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
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class HomeViewModel() : ViewModel() {

    private val database = RecipeDatabase.getDatabase(MyApplication.applicationContext())
    private val repository: RecipeRepository = RecipeRepository(
        ApiClient.apiService, database.recipeDao(), database.ingredientDao(),
        database.equipmentDao()
    )

    //TODO - This init block throws NPE
//    init {
//        getRandomRecipes()
//    }

    val randomRecipes: StateFlow<UiState<List<RecipeResponse>>> = repository.getRandomRecipes()
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(),
            UiState.Loading
        )

//    private val _randomRecipes = MutableStateFlow<UiState<List<RecipeResponse>>>(UiState.Loading)
//    val randomRecipes: StateFlow<UiState<List<RecipeResponse>>> = _randomRecipes.asStateFlow()

    private var isDataLoaded = false;

//     fun getRandomRecipes() {
//
//        if (isDataLoaded) return
//
//        _randomRecipes.value = UiState.Loading
//
//        viewModelScope.launch {
//            repository.getRandomRecipes().collect { result ->
//
//                when (result) {
//                    is UiState.Success -> {
//                        Log.i("HomeViewModel", "getRandomRecipes: Success")
//                        isDataLoaded = true
//                        _randomRecipes.value = result
//                    }
//
//                    is UiState.Error -> {
//                        Log.e("HomeViewModel", "getRandomRecipes: Failed", result.exception)
//                    }
//                    else -> {}
//                }
//            }
//        }
//    }
}
