package com.indivar.core

import com.indivar.models.series.Fixture
import com.indivar.models.series.Series
import com.indivar.models.series.SeriesGroup
import kotlinx.coroutines.flow.MutableSharedFlow


interface Navigator {
    val sharedFlow: MutableSharedFlow<String>
    suspend fun navigateToSeries(seriesGroup: SeriesGroup)
    suspend fun navigateToSeriesFixtures(series: Series)
    suspend fun navigateToFixture(fixture: Fixture)
}