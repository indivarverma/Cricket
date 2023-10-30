package com.indivar.common.adapters

import com.google.gson.Gson
import com.squareup.moshi.FromJson
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.JsonReader
import com.squareup.moshi.JsonWriter
import com.squareup.moshi.ToJson
import java.time.LocalDate
import java.time.LocalTime
import java.time.ZoneId
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter


data class JsonDateTime (
    val date: String?,
    val time: String?,
    val timezone: String?,
)
class ZonedDateTimeAdapter : JsonAdapter<ZonedDateTime>() {

    @FromJson

    override fun fromJson(reader: JsonReader): ZonedDateTime? =
        try {
            val readValue = reader.nextString()
            val jsonDateTime = Gson().fromJson<JsonDateTime>(readValue, JsonDateTime::class.java)
            val date = LocalDate.parse(jsonDateTime.date)
            val time = LocalTime.parse(jsonDateTime.time)
            val timezone = ZoneId.of(jsonDateTime.timezone)
             ZonedDateTime.of(date, time, timezone)

        } catch (_: Exception) {
            null
        }

    @ToJson
    override fun toJson(writer: JsonWriter, input: ZonedDateTime?) {
        val date: String = input?.toLocalDate().toString()
        val time: String = input?.toLocalTime().toString()
        val timezone: String = input?.getZone().toString()
        writer.value(Gson().toJson(JsonDateTime(date, time, timezone)))
    }

    companion object {
        val FORMATTER: DateTimeFormatter = DateTimeFormatter.ISO_ZONED_DATE_TIME
    }
}