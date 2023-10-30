package com.indivar.core.viewmodels

import com.indivar.core.Response
import com.indivar.models.AllSeries

interface PullAllSeriesUseCase {
    suspend fun trigger(): Response<AllSeries>
}