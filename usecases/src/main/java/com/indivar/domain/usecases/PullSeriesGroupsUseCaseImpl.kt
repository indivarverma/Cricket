package com.indivar.domain.usecases

import com.indivar.core.data.Response
import com.indivar.core.series.groups.domain.usecase.PullSeriesGroupsUseCase
import com.indivar.domain.repo.Repository
import com.indivar.models.series.SeriesGroups
import javax.inject.Inject

class PullSeriesGroupsUseCaseImpl @Inject constructor(
    private val networkRepository: Repository
) : PullSeriesGroupsUseCase {
    override suspend fun trigger(): Response<SeriesGroups> = networkRepository.getSeriesGroups()

}