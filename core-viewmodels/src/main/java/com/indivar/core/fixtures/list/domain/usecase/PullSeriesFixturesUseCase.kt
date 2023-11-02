package com.indivar.core.fixtures.list.domain.usecase

interface PullSeriesFixturesUseCase {
    suspend fun fetchSeriesFixtures(seriesId: Int)
}