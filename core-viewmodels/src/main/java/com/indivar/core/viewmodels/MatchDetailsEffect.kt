package com.indivar.core.viewmodels

sealed class MatchDetailsEffect {

    data class Ready(
        val triggerFetch: (Int) -> Unit
    ): MatchDetailsEffect()
    object ToastNow: MatchDetailsEffect()
    data class CheckPlayerDetails(
        val playerId: Int,
    ): MatchDetailsEffect()

}
