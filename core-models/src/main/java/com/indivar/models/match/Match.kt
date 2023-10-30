package com.indivar.models.match

import com.indivar.models.Team


data class Match(
    val id: Int,
    val seriesId: Int,
    val matchDates: MatchDates?,
    val title: String,
    val homeTeam: Team?,
    val awayTeams: List<Team>,
    val result: Boolean,
    val tossResult: String?,
    val homeTeamScores: String?,
    val awayTeamScores: String?,
    val matchOfficials: MatchOfficials?,
    val scoreCard: ScoreCard?,
)
