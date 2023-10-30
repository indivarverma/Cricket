package com.indivar.models

data class AllSeries(
    val title: String,
    val description: String,
    val seriesGroup: List<SeriesGroup>,
)

data class SeriesGroup(
    val type: String,
    val series: List<Series>,
)

data class Series(
    val id: Int,
    val name: String,
    val status: String,
    val season: String,
)