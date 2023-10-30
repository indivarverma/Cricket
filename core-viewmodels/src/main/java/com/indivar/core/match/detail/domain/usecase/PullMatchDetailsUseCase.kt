package com.indivar.core.match.detail.domain.usecase

import com.indivar.models.match.Match
import com.indivar.core.data.Response

interface PullMatchDetailsUseCase {
    suspend fun trigger(matchId: Int): Response<Match>
}