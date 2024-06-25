package com.aspark.networking

import com.aspark.recipeapp.model.Recipe
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {
    @GET("recipes/random")
    suspend fun getRandomRecipes(@Query("apiKey") apiKey: String): Response<List<Recipe>>

    @GET("recipes/complexSearch")
    suspend fun searchRecipes(@Query("apiKey") apiKey: String,
                              @Query("query") query: String): Response<List<Recipe>>
}