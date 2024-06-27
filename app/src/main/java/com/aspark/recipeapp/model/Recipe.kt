package com.aspark.recipeapp.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "recipe")
data class Recipe(
    @PrimaryKey val id: Long,
    val title: String,
    val image: String,
    val isFavorite: Boolean
)
