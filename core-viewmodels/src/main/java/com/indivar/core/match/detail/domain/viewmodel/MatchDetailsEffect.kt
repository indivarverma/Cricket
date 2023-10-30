package com.indivar.core.match.detail.domain.viewmodel

sealed class MatchDetailsEffect {
    object ToastNow: MatchDetailsEffect()
    data class CheckPlayerDetails(
        val playerId: Int,
    ): MatchDetailsEffect()

}
