package com.indivar.core.viewmodels

import com.indivar.models.Match

interface PullMatchDetailsUseCase {
    suspend fun invoke(matchId: Int): Match?
}