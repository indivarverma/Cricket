package com.indivar.domain.usecases

import com.indivar.core.data.Response
import com.indivar.core.series.groups.domain.usecase.PullSeriesGroupsUseCase
import com.indivar.domain.repo.Repository
import com.indivar.models.series.SeriesGroups
import kotlinx.coroutines.coroutineScope
import javax.inject.Inject

class PullSeriesGroupsUseCaseImpl @Inject constructor(
    private val networkRepository: Repository
) : PullSeriesGroupsUseCase {
    override suspend fun trigger(

    ): Response<SeriesGroups> = coroutineScope {
        try {
            networkRepository.getSeriesGroups()?.let {
                Response.Success(it)
            } ?: Response.Error(404, "Empty response")
        } catch (e: Throwable) {
            if (e is DetailedServerError) {
                Response.Error(
                    errorCode = e.errorCode,
                    errorMessage = e.errorMessage,
                )
            } else {
                Response.Error(
                    errorCode = 400,
                    errorMessage = e.message ?: "",
                )
            }
        }

    }


}