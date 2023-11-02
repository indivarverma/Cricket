package com.indivar.domain.repo

import com.indivar.core.data.Response
import com.indivar.models.match.Match
import com.indivar.models.series.SeriesFixtures
import com.indivar.models.series.SeriesGroups

interface Repository {
    suspend fun pullMatchDetails(
        matchId: Int,
    ): Response<Match>

    suspend fun getSeriesGroups(): Response<SeriesGroups>

    suspend fun fetchSeriesFixtures(
        seriesId: Int,
    ): Response<SeriesFixtures>
}

