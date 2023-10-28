package com.indivar.models

data class Team(
    val id: Int,
    val name: String,
    val code: String,
    val players: List<Player>
)
