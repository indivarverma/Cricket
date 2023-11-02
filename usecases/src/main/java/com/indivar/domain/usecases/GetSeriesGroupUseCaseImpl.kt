package com.indivar.domain.usecases

import com.indivar.core.Base64Encoder
import com.indivar.core.series.detail.domain.usecase.GetSeriesGroupUseCase
import com.indivar.models.series.SeriesGroup
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import javax.inject.Inject

class GetSeriesGroupUseCaseImpl @Inject constructor(
    private val base64Encoder: Base64Encoder
): GetSeriesGroupUseCase {

    private val moshi: Moshi = Moshi.Builder().add(KotlinJsonAdapterFactory()).build()
    override suspend fun getSeriesGroup(jsobString: String): SeriesGroup? {
        val decoded = base64Encoder.decode(jsobString)
        return moshi.adapter(SeriesGroup::class.java).fromJson(decoded)
    }
}