package com.indivar.core

interface Base64Encoder {
    fun encode(value: String): String
    fun decode(value: String): String
}