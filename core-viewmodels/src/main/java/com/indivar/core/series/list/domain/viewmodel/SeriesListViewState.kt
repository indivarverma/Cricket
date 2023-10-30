package com.indivar.core.series.list.domain.viewmodel

import com.indivar.models.series.AllSeries

data class SeriesListViewState(
    val allSeries: AllSeries?,
    val showError: Boolean,
    val showLoading: Boolean,
    val refetch: suspend () -> Unit,
) {
    companion object {
        val initial = SeriesListViewState(
            allSeries = null,
            showError = false,
            showLoading = true,
            refetch = {}
        )
    }
}
