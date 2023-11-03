package com.indivar.domain.repo.series.groups

import com.squareup.moshi.Json
import java.time.ZonedDateTime


data class AllSeriesDetail(
    @Json(name = "meta")
    val meta: Meta,
    @Json(name = "results")
    val results: List<SeriesSet>
)

data class Meta(
    @Json(name = "title")
    val title: String,
    @Json(name = "description")
    val description: String,
)

data class SeriesSet(
    @Json(name = "type")
    val type: String,
    @Json(name = "series")
    val items: List<SeriesItem>
)

data class SeriesItem(
    @Json(name = "series_id")
    val id: Int,
    @Json(name = "series_name")
    val name: String,
    @Json(name = "status")
    val status: String,
    @Json(name = "season")
    val season: String,
    @Json(name = "updated_at")
    val updatedAt: ZonedDateTime?,
)
