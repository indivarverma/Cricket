package com.indivar.core.series.list.domain.viewmodel


import com.indivar.models.series.AllSeries
import com.indivar.models.series.SeriesGroup

data class SeriesListViewState(
    val allSeries: AllSeries?,
    val showError: Boolean,
    val showLoading: Boolean,
    val onSeriesItemClicked: (SeriesGroup) -> Unit,
    val refetch: suspend () -> Unit,
) {
    companion object {
        val initial = SeriesListViewState(
            allSeries = null,
            showError = false,
            showLoading = true,
            onSeriesItemClicked = {},
            refetch = {}
        )
    }
}
