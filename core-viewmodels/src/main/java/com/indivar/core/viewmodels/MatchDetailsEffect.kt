package com.indivar.core.viewmodels

sealed class MatchDetailsEffect {
    object ToastNow: MatchDetailsEffect()
    data class CheckPlayerDetails(
        val playerId: Int,
    ): MatchDetailsEffect()

}
