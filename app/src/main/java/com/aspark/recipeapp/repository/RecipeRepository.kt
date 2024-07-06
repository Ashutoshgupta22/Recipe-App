package com.aspark.recipeapp.repository

import android.util.Log
import com.aspark.networking.ApiService
import com.aspark.networking.model.RecipeResponse
import com.aspark.networking.model.SearchSuggestionResponse
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

    suspend fun getSearchSuggestions(query: String): List<SearchSuggestionResponse>? {
        val response = apiService.getSearchSuggestions(query = query, 10)

        return if (response.isSuccessful) {
            Log.d("TAG", "getSearchSuggestions: $response")
            response.body()
        }
        else {
            Log.e("RecipeRepository", "getSearchSuggestions: Response unsuccessful" )
            null
        }
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