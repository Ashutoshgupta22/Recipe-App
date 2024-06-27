package com.aspark.recipeapp.room

import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import android.content.Context
import com.aspark.recipeapp.model.Recipe

@Database(entities = [Recipe::class], version = 1)
abstract class RecipeDatabase : RoomDatabase() {
    abstract fun recipeDao(): RecipeDao

    companion object {
        @Volatile private var instance: RecipeDatabase? = null

        fun getDatabase(context: Context): RecipeDatabase =
            instance ?: synchronized(this) {
                instance ?: buildDatabase(context).also { instance = it }
            }

        private fun buildDatabase(context: Context) =
            Room.databaseBuilder(context.applicationContext, RecipeDatabase::class.java, "recipes.db").build()
    }
}
