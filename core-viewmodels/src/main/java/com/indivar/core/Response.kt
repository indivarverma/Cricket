package com.indivar.core



sealed class Response<T> {
    data class Success<T>(
        val data: T
    ): Response<T>()

    data class Error<T>(
        val errorCode: Int,
        val errorMessage: String,
    ): Response<T>()
}
