package com.indivar.models.series

data class AllSeries(
    val title: String,
    val description: String,
    val seriesGroup: List<SeriesGroup>,
)

