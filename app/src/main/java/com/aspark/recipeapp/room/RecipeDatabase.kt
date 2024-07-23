package com.aspark.recipeapp.room

import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import android.content.Context

@Database(entities = [RecipeEntity::class, IngredientEntity::class, EquipmentEntity::class],
    version = 3)
abstract class RecipeDatabase : RoomDatabase() {
    abstract fun recipeDao(): RecipeDao
    abstract fun ingredientDao(): IngredientDao
    abstract fun equipmentDao(): EquipmentDao

    companion object {
        @Volatile private var instance: RecipeDatabase? = null

        fun getDatabase(context: Context): RecipeDatabase =
            instance ?: synchronized(this) {
                instance ?: buildDatabase(context).also { instance = it }
            }

        private fun buildDatabase(context: Context) =
            Room.databaseBuilder(context.applicationContext, RecipeDatabase::class.java,
                "recipes.db")
                .fallbackToDestructiveMigration()
                .build()
    }
}
