package com.indivar.domain.repo

import com.indivar.models.match.Match
import com.indivar.models.series.SeriesGroups

interface Repository {
    suspend fun pullMatchDetails(
        matchId: Int,
    ): Match?

    suspend fun getSeriesGroups(): SeriesGroups?
}

