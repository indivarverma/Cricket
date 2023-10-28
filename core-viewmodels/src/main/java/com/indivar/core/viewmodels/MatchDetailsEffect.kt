package com.indivar.core.viewmodels

sealed class MatchDetailsEffect {

    data class Ready(
        val triggerFetch: suspend (Int) -> Unit
    ): MatchDetailsEffect()
    object ToastNow: MatchDetailsEffect()
    data class CheckPlayerDetails(
        val playerId: String,
    ): MatchDetailsEffect()

}
