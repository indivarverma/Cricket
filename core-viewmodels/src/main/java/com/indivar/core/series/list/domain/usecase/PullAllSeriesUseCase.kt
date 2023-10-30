package com.indivar.core.series.list.domain.usecase

import com.indivar.core.data.Response
import com.indivar.models.series.AllSeries

interface PullAllSeriesUseCase {
    suspend fun trigger(): Response<AllSeries>
}