package com.aspark.networking

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {
    @GET("recipes/random")
    suspend fun getRandomRecipes(
        @Query("apiKey") apiKey: String = BuildConfig.API_KEY,
        @Query("number") number: Int = 10
    ): Response<ObjectResponse>

    @GET("recipes/complexSearch")
    suspend fun searchRecipes(
        @Query("apiKey") apiKey: String = BuildConfig.API_KEY,
        @Query("query") query: String): Response<List<RecipeResponse>>

    @GET("recipes/{id}/information")
    suspend fun getRecipeById(
        @Path("id") id: Long,
        @Query("apiKey") apiKey: String = BuildConfig.API_KEY
    ): Response<RecipeResponse>
}