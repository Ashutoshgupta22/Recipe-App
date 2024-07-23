package com.aspark.recipeapp

import java.lang.Exception

sealed class MyResult<out T> {
    data class Success<out T>(val data: T): MyResult<T>()
    data class Failure(val exception: Exception): MyResult<Nothing>()
    data object Loading : MyResult<Nothing>()
}