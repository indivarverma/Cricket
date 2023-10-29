package com.indivar.core.viewmodels

import com.indivar.models.Match
import com.indivar.core.Response

interface PullMatchDetailsUseCase {
    suspend fun trigger(matchId: Int): Response<Match>
}