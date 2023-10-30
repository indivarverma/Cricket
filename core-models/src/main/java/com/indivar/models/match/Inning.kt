package com.indivar.models.match

import com.indivar.models.Player

data class Inning(
    val title: String,
    val runs: Int,
    val overs: Overs,
    val wickets: Int,
    val extras: Int,
    val extraTitle: String,
    val fow: String,
    val batting: List<Pair<Player, BattingMatchStat>>,
    val bowling: List<Pair<Player, BowlingMatchStat>>,
)