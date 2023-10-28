package com.indivar.core.viewmodels

import com.indivar.models.Match
import com.indivar.core.Response

interface PullMatchDetailsUseCase {
    suspend fun invoke(matchId: Int): Response<Match>
}