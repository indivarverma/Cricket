package com.indivar.domain.usecases

import com.indivar.core.fixtures.list.domain.usecase.PullSeriesFixturesUseCase
import com.indivar.domain.repo.Repository
import javax.inject.Inject

class PullSeriesFixturesUseCaseImpl @Inject constructor(
    val networkRepository: Repository,
): PullSeriesFixturesUseCase {
    override suspend fun fetchSeriesFixtures(seriesId: Int) {
        networkRepository.fetchSeriesFixtures(seriesId)
    }
}