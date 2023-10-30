package com.indivar.models.match

data class Overs(
    val completeOvers: Int,
    val ballsInCurrentOver: Int,
) {
    companion object {
        val None = Overs(completeOvers = 0, ballsInCurrentOver = 0)
    }
}