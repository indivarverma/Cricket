package com.indivar.repository

import kotlinx.coroutines.flow.Flow
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Path

interface NetworkApi {
    @GET("/match/{matchId}")
    suspend fun getMatchDetails(
        @Path("matchId") matchId: Int,
    ): MatchDetail


    @GET("/series")
    suspend fun getAllSeries(

    ): AllSeriesDetail
}