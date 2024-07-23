package com.aspark.recipeapp.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "recipe")
data class RecipeEntity(
    @PrimaryKey val id: Long,
    val title: String,
    val image: String,
    val readyInMinutes: Int,
    val servings: Int,
    val pricePerServing: Double,
    val instructions: String,
    val summary: String,
    val isFavorite: Boolean
)

@Entity(tableName = "ingredients")
data class IngredientEntity(
    @PrimaryKey(autoGenerate = true) val ingredientId: Long = 0,
    val recipeId: Long,
    val name: String,
    val amount: Double,
    val unit: String
)

@Entity(tableName = "equipment")
data class EquipmentEntity(
    @PrimaryKey(autoGenerate = true) val equipmentId: Long = 0,
    val recipeId: Long,
    val name: String,
    val image: String
)
