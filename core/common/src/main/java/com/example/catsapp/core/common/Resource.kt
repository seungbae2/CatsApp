package com.example.catsapp.core.common

sealed class Resource<T>(
    val data: T? = null,
    val error: Throwable? = null
) {
    class Loading<T>                       : Resource<T>()
    class Success<T>(data: T)              : Resource<T>(data = data)
    class Error<T>(t: Throwable, data: T?) : Resource<T>(data = data, error = t)
}