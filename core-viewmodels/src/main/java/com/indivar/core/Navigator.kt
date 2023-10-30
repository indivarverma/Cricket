package com.indivar.core

import com.indivar.models.series.SeriesGroup
import kotlinx.coroutines.flow.MutableSharedFlow


interface Navigator {
    val sharedFlow: MutableSharedFlow<String>
    suspend fun navigateToSeries(seriesGroup: SeriesGroup)
}