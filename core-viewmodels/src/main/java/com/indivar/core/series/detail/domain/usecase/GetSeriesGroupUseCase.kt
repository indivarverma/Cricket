package com.indivar.core.series.detail.domain.usecase

import com.indivar.models.series.SeriesGroup

interface GetSeriesGroupUseCase {
    suspend fun getSeriesGroup(jsobString: String): SeriesGroup?
}