package com.indivar.models.match

import com.indivar.models.Boundary

data class BattingMatchStat(
    val battingOrder: Int,
    val outStyle: String?,
    val minutesOfPlay: Int?,
    val runs: Int,
    val balls: Int,
    val boundaries: List<Boundary>,
    val strikeRate: Float?,
)


