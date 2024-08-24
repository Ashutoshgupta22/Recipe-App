package com.aspark.recipeapp

import java.lang.Exception

sealed class UiState<out T> {
    data class Success<out T>(val data: T): UiState<T>()
    data class Failure(val exception: Exception): UiState<Nothing>()
    data object Loading : UiState<Nothing>()
    data object Idle: UiState<Nothing>()
}