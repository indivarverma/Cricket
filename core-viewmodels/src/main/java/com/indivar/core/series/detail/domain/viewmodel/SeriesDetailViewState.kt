package com.indivar.core.series.detail.domain.viewmodel

import com.indivar.models.series.Series

data class SeriesDetailViewState (
    val title: String,
    val list: List<Series>,
    val onSeriesSelected: Series.() -> Unit
)