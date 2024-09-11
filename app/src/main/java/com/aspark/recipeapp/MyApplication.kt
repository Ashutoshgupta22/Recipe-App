package com.aspark.recipeapp

import android.app.Application
import com.aspark.recipeapp.ui.MainActivity

class MyApplication : Application() {

    init {
        instance = this
    }

    companion object {
        private var instance: MyApplication? = null

        fun applicationContext(): MyApplication {
            return instance as MyApplication
        }
    }
}
