package com.indivar.usecases

import com.indivar.models.Match
import kotlinx.coroutines.flow.Flow

interface Repository {
    suspend fun pullMatchDetails(
        matchId: Int,
    ): Match?
}