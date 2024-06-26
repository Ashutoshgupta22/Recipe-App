package com.aspark.networking

data class ObjectResponse(
    val recipes: MutableList<RecipeResponse>
)
data class RecipeResponse(
    val id: Long,
    val title: String,
    val image: String
)
