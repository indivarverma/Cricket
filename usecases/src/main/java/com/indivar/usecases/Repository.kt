package com.indivar.usecases

import com.indivar.models.AllSeries
import com.indivar.models.Match
import com.indivar.models.Player
import kotlinx.coroutines.flow.Flow
import javax.inject.Qualifier

interface Repository {
    suspend fun pullMatchDetails(
        matchId: Int,
    ): Match?

    suspend fun getAllSeries(): AllSeries?
}

