package com.indivar.domain.repo

import com.indivar.models.match.Match
import com.indivar.models.series.AllSeries

interface Repository {
    suspend fun pullMatchDetails(
        matchId: Int,
    ): Match?

    suspend fun getAllSeries(): AllSeries?
}

