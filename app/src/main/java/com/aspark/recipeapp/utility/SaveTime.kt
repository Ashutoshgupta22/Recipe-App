package com.aspark.recipeapp.utility

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.map

class SaveTime(private val context: Context) {

    companion object {
        private val Context.datastore: DataStore<Preferences> by
        preferencesDataStore(name = "time_prefs")
        private val STORED_TIME_KEY = longPreferencesKey(name = "time")
    }

    suspend fun saveCurrentTime() {
        val currentTime = System.currentTimeMillis()
        context.datastore.edit { prefs ->
            prefs[STORED_TIME_KEY] = currentTime
        }
    }

    fun getStoredTime() = context.datastore.data.map {prefs ->
        prefs[STORED_TIME_KEY] ?: 0
    }
}