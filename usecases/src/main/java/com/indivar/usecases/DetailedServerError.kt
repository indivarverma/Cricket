package com.indivar.usecases

data class DetailedServerError(
    val errorCode: Int,
    val errorMessage: String,
) : Throwable(errorMessage)