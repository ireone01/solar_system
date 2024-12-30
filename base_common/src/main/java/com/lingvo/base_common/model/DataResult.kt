package com.lingvo.base_common.model

sealed class DataResult<out T> {

    data object Loading : DataResult<Nothing>()

    data class Success<out T>(val data: T) : DataResult<T>()

    data class Error(val exception: Exception? = null) : DataResult<Nothing>()

}