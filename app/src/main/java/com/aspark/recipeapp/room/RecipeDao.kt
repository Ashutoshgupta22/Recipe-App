package com.aspark.recipeapp.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.aspark.recipeapp.model.Recipe

@Dao
interface RecipeDao {
    @Query("SELECT * FROM recipe WHERE isFavorite = 1")
    suspend fun getFavoriteRecipes(): List<Recipe>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRecipe(recipe: Recipe)
}