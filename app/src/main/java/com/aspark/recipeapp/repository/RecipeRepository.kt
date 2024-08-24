package com.aspark.recipeapp.repository

import android.util.Log
import com.aspark.networking.ApiService
import com.aspark.networking.model.RecipeResponse
import com.aspark.networking.model.SearchSuggestionResponse
import com.aspark.recipeapp.MyApplication
import com.aspark.recipeapp.UiState
import com.aspark.recipeapp.room.EquipmentEntity
import com.aspark.recipeapp.room.IngredientEntity
import com.aspark.recipeapp.room.RecipeEntity
import com.aspark.recipeapp.room.EquipmentDao
import com.aspark.recipeapp.room.IngredientDao
import com.aspark.recipeapp.room.RecipeDao
import com.aspark.recipeapp.room.toEntity
import com.aspark.recipeapp.room.toRecipeResponse
import com.aspark.recipeapp.utility.SaveTime
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.flow
import okhttp3.internal.toImmutableList

class RecipeRepository(
    private val apiService: ApiService,
    private val recipeDao: RecipeDao,
    private val ingredientDao: IngredientDao,
    private val equipmentDao: EquipmentDao
) {
    private val saveTime by lazy {  SaveTime(MyApplication.applicationContext()) }

    // Function to get recipes, first from cache then from network
    fun getRandomRecipes(): Flow<UiState<List<RecipeResponse>>> = flow {

        // Emit cached data first
        emit(UiState.Success(getCachedRandomRecipes()))

        if (isCacheOld()) { //fetch from remote if cache is old
            try {
                val remoteResponse = apiService.getRandomRecipes(number = 25)
                val remoteRecipes = remoteResponse.body()?.recipes?.toImmutableList()!!

                updateCache(remoteRecipes)
                emit(UiState.Success(remoteRecipes))
            } catch (e: Exception) {
                if (getCachedRandomRecipes().isEmpty())
                    emit(UiState.Failure(e))
            }
        }
    }

    private suspend fun getCachedRandomRecipes(): List<RecipeResponse> {
        Log.i("RecipeRepository", "getCachedRandomRecipes: getting from cache")
        return recipeDao.getAllRecipes().map { recipeEntity ->
            val ingredients = ingredientDao.getIngredientsForRecipe(recipeEntity.id)
            val equipments = equipmentDao.getEquipmentForRecipe(recipeEntity.id)
            recipeEntity.toRecipeResponse(ingredients, equipments)
        }
    }

    private suspend fun updateCache(recipes: List<RecipeResponse>) {
        Log.i("RecipeRepository", "updateCache: cache is old, updating cache")
        saveTime.saveCurrentTime()

        recipeDao.deleteCachedRecipes()
        recipes.forEach {
            insertRecipe(it)
        }
    }

    private suspend fun insertRecipe(recipe: RecipeResponse) {
//        val recipeEntity = RecipeEntity(
//            id = recipe.id,
//            title = recipe.title,
//            image = recipe.image,
//            readyInMinutes = recipe.readyInMinutes,
//            servings = recipe.servings,
//            pricePerServing = recipe.pricePerServing,
//            instructions = recipe.instructions,
//            summary = recipe.summary,
//            isFavorite = false
//        )
        recipeDao.insertRecipe(recipe.toEntity())

        val ingredientEntities = recipe.extendedIngredients.map { ingredient ->
            IngredientEntity(
                recipeId = recipe.id,
                name = ingredient.name,
                amount = ingredient.amount,
                unit = ingredient.unit
            )
        }
        ingredientDao.insertIngredients(ingredientEntities)

        val equipmentEntities = recipe.equipment.map { equipment ->
            EquipmentEntity(
                recipeId = recipe.id,
                name = equipment.name,
                image = equipment.image
            )
        }
        equipmentDao.insertEquipment(equipmentEntities)
    }

     fun getSearchSuggestions(query: String): Flow<UiState<List<SearchSuggestionResponse>>> = flow{

         try {
             val response = apiService.getSearchSuggestions(query = query, 25)

             if (response.isSuccessful && response.body() != null) {
                 if (response.body()!!.isEmpty())
                     emit(UiState.Failure(Exception("No match found")))
                 else
                     emit(UiState.Success(response.body()!!))
             }
             else {
                 Log.e("RecipeRepository", "getSearchSuggestions: Response unsuccessful")
                 emit(UiState.Failure(Exception("Response unsuccessful")))
             }
         }
         catch (e: Exception) {
             Log.e("RecipeRepository", "getSearchSuggestions: Exception", e)
             emit(UiState.Failure(e))
         }
    }

    fun getRecipeById(recipeId: Long): Flow<UiState<RecipeResponse>> = flow {

        try {
            Log.i("Repo", "getRecipeById: fetching recipe by id")
            val recipe = apiService.getRecipeById(id = recipeId).body()!!

            emit(UiState.Success(recipe))
        } catch (e: Exception) {
            emit(UiState.Failure(e))
        }
    }

    suspend fun getFavoriteRecipes() = recipeDao.getFavoriteRecipes()

    suspend fun addToFavorites(recipe: RecipeEntity) {
        recipeDao.insertRecipe(recipe)
    }

    suspend fun deleteFavoriteRecipe(recipeId: Long) {
        recipeDao.deleteFavoriteRecipe(recipeId)
    }

    suspend fun checkIfFavorite(recipeId: Long): Boolean = recipeDao.checkIfFavorite(recipeId)

    private suspend fun isCacheOld(): Boolean {
        val currentTime = System.currentTimeMillis()
        val storedTime = saveTime.getStoredTime().firstOrNull() ?: return true
        val thresholdTime = 15 * 60 * 1000L    // 15 minutes

        return currentTime.minus(storedTime) > thresholdTime
    }
}
