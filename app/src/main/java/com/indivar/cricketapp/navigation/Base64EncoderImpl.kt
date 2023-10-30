package com.indivar.cricketapp.navigation

import android.util.Base64
import java.nio.charset.Charset
import javax.inject.Inject
import javax.inject.Singleton


class Base64EncoderImpl @Inject constructor() : Base64Encoder {
    override fun encode(value: String): String {
        val ct = value.toByteArray(Charset.forName("UTF-8"))
        return Base64.encodeToString(ct, Base64.NO_WRAP)
    }

    override fun decode(value: String): String {
        val ct = value.toByteArray(Charset.forName("UTF-8"))
        val byteArray = Base64.decode(ct, Base64.NO_WRAP)
        return byteArray.toString(Charsets.UTF_8)
    }
}