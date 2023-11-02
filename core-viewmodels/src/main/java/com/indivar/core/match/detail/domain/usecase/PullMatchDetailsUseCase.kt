package com.indivar.core.match.detail.domain.usecase


import com.indivar.core.data.Response
import com.indivar.models.match.Match

interface PullMatchDetailsUseCase {
    suspend fun trigger(matchId: Int): Response<Match>
}