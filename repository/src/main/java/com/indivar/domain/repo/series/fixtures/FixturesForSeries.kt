package com.indivar.domain.repo.series.fixtures

import com.indivar.domain.repo.match.details.Team
import com.squareup.moshi.Json
import java.time.LocalDateTime
import java.time.ZonedDateTime

data class FixturesForSeries(
    @Json(name = "results")
    val results: List<FixtureItem>
)

data class FixtureItem(
    @Json(name = "id")
    val id: Int,
    @Json(name = "match_title")
    val title: String,
    @Json(name = "away")
    val away: Team,
    @Json(name = "home")
    val home: Team,
    @Json(name = "match_subtitle")
    val subtitle: String,
    @Json(name = "result")
    val result: String,
    @Json(name = "series_id")
    val seriesId: Int,
    @Json(name = "status")
    val status: String,
    @Json(name = "venue")
    val venue: String,
    @Json(name = "date")
    val date: ZonedDateTime?,
)