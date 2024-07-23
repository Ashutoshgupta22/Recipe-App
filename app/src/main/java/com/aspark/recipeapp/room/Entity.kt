package com.aspark.recipeapp.room

import androidx.room.Delete
import androidx.room.Entity
import androidx.room.ForeignKey
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

@Entity(
    tableName = "ingredients",
    foreignKeys = [
        ForeignKey(
            entity = RecipeEntity::class,
            parentColumns = ["id"],
            childColumns = ["recipeId"],
            onDelete = ForeignKey.CASCADE
        )
    ]
    )
data class IngredientEntity(
    @PrimaryKey(autoGenerate = true) val ingredientId: Long = 0,
    val recipeId: Long,
    val name: String,
    val amount: Double,
    val unit: String
)

@Entity(
    tableName = "equipment",
    foreignKeys = [
        ForeignKey(
            entity = RecipeEntity::class,
            parentColumns = ["id"],
            childColumns = ["recipeId"],
            onDelete = ForeignKey.CASCADE
        )
    ]
    )
data class EquipmentEntity(
    @PrimaryKey(autoGenerate = true) val equipmentId: Long = 0,
    val recipeId: Long,
    val name: String,
    val image: String
)
