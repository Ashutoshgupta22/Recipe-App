package com.aspark.networking

import com.aspark.networking.model.ObjectResponse
import com.aspark.networking.model.RecipeResponse
import com.aspark.networking.model.SearchSuggestionResponse
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
        @Query("query") query: String): Response<List<SearchSuggestionResponse>>

    @GET("recipes/{id}/information")
    suspend fun getRecipeById(
        @Path("id") id: Long,
        @Query("apiKey") apiKey: String = BuildConfig.API_KEY
    ): Response<RecipeResponse>

    @GET("recipes/autocomplete")
    suspend fun getSearchSuggestions(
        @Query("query") query: String,
        @Query("number") number: Int,
        @Query("apiKey") apiKey: String = BuildConfig.API_KEY
    ): Response<List<SearchSuggestionResponse>>
}