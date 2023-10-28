package com.indivar.repository

data class DetailedServerError(
    val errorCode: Int,
    val errorMessage: String,
) : Throwable(errorMessage)
