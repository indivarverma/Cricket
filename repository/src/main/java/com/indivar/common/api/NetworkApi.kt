package com.indivar.common.api

import com.indivar.domain.repo.match.details.MatchDetail
import com.indivar.domain.repo.series.fixtures.FixturesForSeries
import com.indivar.domain.repo.series.groups.AllSeriesDetail
import retrofit2.http.GET
import retrofit2.http.Path

interface NetworkApi {
    @GET("/match/{matchId}")
    suspend fun getMatchDetails(
        @Path("matchId") matchId: Int,
    ): MatchDetail


    @GET("/series")
    suspend fun getAllSeries(

    ): AllSeriesDetail

    @GET("/fixtures-by-series/{seriesId}")
    suspend fun getSeriesFixtures(
        @Path("seriesId") seriesId: Int,
    ): FixturesForSeries
}