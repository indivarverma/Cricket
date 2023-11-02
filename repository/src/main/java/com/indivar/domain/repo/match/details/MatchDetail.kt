package com.indivar.domain.repo.match.details


import com.squareup.moshi.Json
import java.time.ZonedDateTime

data class MatchDetail(
    val results: Results?,
)

data class Results(
    val fixture: ResultFixture,
    @Json(name = "live_details")
    val liveDetails: LiveDetails?,

    )

data class ResultFixture(
    val id: Int,
    val series_id: Int,
    val match_title: String,
    val start_date: ZonedDateTime?,
    val end_date: ZonedDateTime?,
    val home: Team,
    val away: Team,
)

data class LiveDetails(
    val match_summary: MatchSummary,
    val officials: Officials,
    val scorecard: List<MatchInning>?,
)

data class MatchSummary(
    val in_play: String,
    val result: String,
    val status: String,
    val toss: String?,
    val home_scores: String,
    val away_scores: String,
)

data class Team(
    val id: Int,
    val name: String,
    val code: String,
)

data class Officials(
    val umpire_1: String,
    val umpire_2: String,
    val umpire_tv: String,
    val referee: String,
    val umpire_reserve: String?,
)

data class MatchInning(
    val innings_number: Int,
    val current: Boolean,
    val title: String,
    val overs: String,
    val runs: Int,
    val wickets: String,
    val extras: Int,
    val extras_detail: String,
    val fow: String,
    val batting: List<BattingStat>,
    val bowling: List<BowlingStat>
)

data class BattingStat(
    val bat_order: Int,
    val player_id: Int,
    val player_name: String,
    val how_out: String?,
    val minutes: String,
    val runs: Int,
    val balls: Int,
    val fours: Int,
    val sixes: Int,
    val strike_rate: String,

    )
data class BowlingStat(
    val player_id: Int,
    val player_name: String,
    val overs: String,
    val maidens: Int,
    val runs_conceded: Int,
    val wickets: Int,
    val economy: String,
    val dot_balls: Int,
    val fours: Int,
    val sixes: Int,
    val extras: String,
)