package com.aspark.recipeapp.repository

import com.aspark.networking.ApiService
import com.aspark.networking.RecipeResponse
import retrofit2.Response

class RecipeRepository(private val apiService: ApiService) {

    suspend fun getRandomRecipes(apiKey: String): Response<List<RecipeResponse>> {
        return apiService.getRandomRecipes(apiKey)
    }

    suspend fun searchRecipes(apiKey: String, query: String): Response<List<RecipeResponse>>{
        return apiService.searchRecipes(apiKey, query)
    }
}