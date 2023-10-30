package com.indivar.models.match

import com.indivar.models.Boundary

data class BowlingMatchStat(
    val overs: Int,
    val maidens: Int,
    val runs: Int,
    val wickets: Int,
    val economy: Float?,
    val dotBalls: Int,
    val boundaries: List<Boundary>,
    val extras: Int,
)
