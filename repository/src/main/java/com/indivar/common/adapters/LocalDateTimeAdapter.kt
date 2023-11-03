package com.indivar.common.adapters

import com.google.gson.Gson
import com.google.gson.JsonSyntaxException
import com.squareup.moshi.FromJson
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.JsonReader
import com.squareup.moshi.JsonWriter
import com.squareup.moshi.ToJson
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter


data class JsonLocalDateTime(
    val date: String?,
    val time: String?,

    )

class LocalDateTimeAdapter : JsonAdapter<LocalDateTime>() {

    @FromJson

    override fun fromJson(reader: JsonReader): LocalDateTime? =
        try {
            val readValue = reader.nextString()
            try {
                val jsonDateTime =
                    Gson().fromJson<JsonLocalDateTime>(readValue, JsonLocalDateTime::class.java)
                val date = LocalDate.parse(jsonDateTime.date)
                val time = LocalTime.parse(jsonDateTime.time)

                LocalDateTime.of(date, time)
            } catch (e: JsonSyntaxException) {
                LocalDateTime.parse(readValue)
            }

        } catch (e: Exception) {
            null
        }

    @ToJson
    override fun toJson(writer: JsonWriter, input: LocalDateTime?) {
        val date: String = input?.toLocalDate().toString()
        val time: String = input?.toLocalTime().toString()

        writer.value(Gson().toJson(JsonLocalDateTime(date, time)))
    }

    companion object {
        val FORMATTER: DateTimeFormatter = DateTimeFormatter.ISO_ZONED_DATE_TIME
    }
}