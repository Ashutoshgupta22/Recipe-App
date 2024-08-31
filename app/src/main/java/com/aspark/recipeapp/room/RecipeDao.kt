package com.aspark.recipeapp.room

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface RecipeDao {

    @Query("select * from recipe")
    suspend fun getAllRecipes(): List<RecipeEntity>

    @Query("SELECT * FROM recipe WHERE isFavorite = 1")
    fun getFavoriteRecipes(): Flow<List<RecipeEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRecipe(recipe: RecipeEntity)

    @Query("select * from recipe where id = :recipeId")
    suspend fun getRecipeById(recipeId: Long): RecipeEntity

    @Query("delete from recipe where isFavorite = 0")
    suspend fun deleteCachedRecipes(): Int

    @Query("delete from recipe where id = :recipeId")
    suspend fun deleteFavoriteRecipe(recipeId: Long)

    @Query("select exists(select 1 from recipe where id = :recipeId and isFavorite = 1)")
    suspend fun checkIfFavorite(recipeId: Long): Boolean
}

@Dao
interface IngredientDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertIngredients(ingredients: List<IngredientEntity>)

    @Query("SELECT * FROM ingredients WHERE recipeId = :recipeId")
    suspend fun getIngredientsForRecipe(recipeId: Long): List<IngredientEntity>
}

@Dao
interface EquipmentDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertEquipment(equipment: List<EquipmentEntity>)

    @Query("SELECT * FROM equipment WHERE recipeId = :recipeId")
    suspend fun getEquipmentForRecipe(recipeId: Long): List<EquipmentEntity>
}