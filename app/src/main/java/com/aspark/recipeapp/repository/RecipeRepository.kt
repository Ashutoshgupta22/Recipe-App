package com.aspark.recipeapp.repository

import android.util.Log
import com.aspark.networking.ApiService
import com.aspark.networking.ObjectResponse
import com.aspark.networking.RecipeResponse
import retrofit2.Response

class RecipeRepository(private val apiService: ApiService) {

    suspend fun getRandomRecipes(): List<RecipeResponse>? {

        val response = apiService.getRandomRecipes()

        return if (response.isSuccessful) {
            Log.i("TAG", "getRandomRecipes: success - ${response.body()?.recipes}")
            response.body()?.recipes
        }
        else null
    }

    suspend fun searchRecipes(query: String): Response<List<RecipeResponse>>{
        return apiService.searchRecipes(query = query)
    }
}