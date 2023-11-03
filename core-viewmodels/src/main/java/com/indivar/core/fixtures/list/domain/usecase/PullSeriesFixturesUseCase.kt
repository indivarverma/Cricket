package com.indivar.core.fixtures.list.domain.usecase

import com.indivar.core.data.Response
import com.indivar.models.series.SeriesFixtures

interface PullSeriesFixturesUseCase {
    suspend fun fetchSeriesFixtures(seriesId: Int): Response<SeriesFixtures>
}