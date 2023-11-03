package com.indivar.models.series

import com.indivar.models.Team
import java.time.ZonedDateTime

data class Fixture(
    val id: Int,
    val title: String,
    val away: Team,
    val home: Team,
    val subtitle: String,
    val result: String,
    val seriesId: Int,
    val status: String,
    val venue: String,
    val date: ZonedDateTime?,
)