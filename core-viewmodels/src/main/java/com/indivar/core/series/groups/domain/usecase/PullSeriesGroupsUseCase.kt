package com.indivar.core.series.groups.domain.usecase

import com.indivar.core.data.Response
import com.indivar.models.series.SeriesGroups

interface PullSeriesGroupsUseCase {
    suspend fun trigger(): Response<SeriesGroups>
}