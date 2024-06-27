package com.aspark.recipeapp.viewmodel

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aspark.networking.ApiClient
import com.aspark.networking.RecipeResponse
import com.aspark.recipeapp.repository.RecipeRepository
import kotlinx.coroutines.launch

class RecipeDetailViewModel: ViewModel() {
    private val repository: RecipeRepository = RecipeRepository(ApiClient.apiService )

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
}