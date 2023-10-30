package com.indivar.cricketapp.navigation

interface Base64Encoder {
    fun encode(value: String): String
    fun decode(value: String): String
}