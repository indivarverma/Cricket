package com.indivar.core.match.detail.domain.viewmodel

import com.indivar.models.match.ScoreCard

data class MatchViewState(
    val showLoading: Boolean,
    val showError: Boolean,
    val info: MatchInformation?,
    val refetch: () -> Unit,
) {
    companion object {
        val initial = MatchViewState(
            showLoading = true,
            showError = false,
            info = null,
            refetch = {}
        )
    }
}

data class MatchInformation(
    val startDate: String,
    val endDate: String,
    val title: String,
    val homeTeam: String,
    val awayTeam: String,
    val result: Boolean,
    val tossResult: String?,
    val homeTeamScores: String?,
    val awayTeamScores: String?,
    val firstEmpire: String?,
    val secondEmpire: String?,
    val thirdEmpire: String?,
    val scoreCard: ScoreCard?,
)