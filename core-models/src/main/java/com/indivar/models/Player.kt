package com.indivar.models

data class Player(
    val id: Int,
    val name: String,
)


enum class PlayingPosition {
    Bowler,
    Allrounder,
    Wicketkeeper,
    Unknown
}