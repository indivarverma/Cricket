package com.indivar.core.series.groups.domain.viewmodel


import com.indivar.models.series.SeriesGroups
import com.indivar.models.series.SeriesGroup

data class SeriesGroupsViewState(
    val seriesGroups: SeriesGroups?,
    val error: ErrorState?,
    val showLoading: Boolean,
    val onSeriesItemClicked: (SeriesGroup) -> Unit,
    val refetch: () -> Unit,
) {
    companion object {
        val initial = SeriesGroupsViewState(
            seriesGroups = null,
            error = null,
            showLoading = true,
            onSeriesItemClicked = {},
            refetch = {}
        )
    }
}


data class ErrorState(
    val code: Int,
    val message: String,
)