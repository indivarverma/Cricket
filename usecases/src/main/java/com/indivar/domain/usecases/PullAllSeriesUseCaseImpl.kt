package com.indivar.domain.usecases

import com.indivar.core.data.Response
import com.indivar.core.series.list.domain.usecase.PullAllSeriesUseCase
import com.indivar.domain.repo.Repository
import com.indivar.models.series.AllSeries
import kotlinx.coroutines.coroutineScope
import javax.inject.Inject

class PullAllSeriesUseCaseImpl @Inject constructor(
    private val networkRepository: Repository
) : PullAllSeriesUseCase {
    override suspend fun trigger(

    ): Response<AllSeries> = coroutineScope {
        try {
            networkRepository.getAllSeries()?.let {
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