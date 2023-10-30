package com.indivar.domain.usecases

data class DetailedServerError(
    val errorCode: Int,
    val errorMessage: String,
) : Throwable(errorMessage)