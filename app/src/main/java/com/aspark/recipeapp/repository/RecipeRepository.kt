package com.aspark.recipeapp.repository

import android.util.Log
import com.aspark.networking.ApiService
import com.aspark.networking.RecipeResponse
import com.aspark.recipeapp.model.Recipe
import com.aspark.recipeapp.room.RecipeDao
import retrofit2.Response

class RecipeRepository(
    private val apiService: ApiService,
    private val recipeDao: RecipeDao?
    ) {

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

    suspend fun getRecipeById(id: Long): RecipeResponse? {
       val response = apiService.getRecipeById(id = id)

        return if (response.isSuccessful) {
            Log.i("TAG", "getRecipeById: success - ${response.body()}")
            response.body()
        }
        else null
    }

    suspend fun getFavoriteRecipes() = recipeDao?.getFavoriteRecipes()


    suspend fun addToFavorites(recipe: Recipe) {
        recipeDao?.insertRecipe(recipe)
    }
}