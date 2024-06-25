package com.aspark.networking

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {
    @GET("recipes/random")
    suspend fun getRandomRecipes(@Query("apiKey") apiKey: String): Response<List<RecipeResponse>>

    @GET("recipes/complexSearch")
    suspend fun searchRecipes(@Query("apiKey") apiKey: String,
                              @Query("query") query: String): Response<List<RecipeResponse>>
}