package com.indivar.models

data class ScoreCard(
    val innings: List<Inning>
)

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

data class Overs(
    val completeOvers: Int,
    val ballsInCurrentOver: Int,
) {
    companion object {
        val None = Overs(completeOvers = 0, ballsInCurrentOver = 0)
    }
}
