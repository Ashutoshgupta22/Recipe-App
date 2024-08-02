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
import com.aspark.recipeapp.MyResult
import com.aspark.recipeapp.room.RecipeEntity
import com.aspark.recipeapp.repository.RecipeRepository
import com.aspark.recipeapp.room.RecipeDatabase
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
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

    val recipe: StateFlow<MyResult<RecipeResponse>> = _recipeId
        .filterNotNull()
        .flatMapLatest { id ->
             repository.getRecipeById(id)

//            flow {
//                emit(MyResult.Loading)
//
//                try {
//                    emit(MyResult.Success(recipe))
//                } catch (e: Exception) {
//                    emit(MyResult.Failure(e))
//                }
//            }
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.Lazily,
            initialValue = MyResult.Loading
        )

    fun getRecipeById(id: Long) {
        _recipeId.value = id
    }

    fun addToFavorites(recipe: RecipeEntity) = viewModelScope.launch {
        repository.addToFavorites(recipe)
    }
}