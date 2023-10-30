package com.indivar.cricketapp.navigation

import android.util.Log
import com.indivar.core.Base64Encoder
import com.indivar.core.Navigator
import com.indivar.models.series.SeriesGroup
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import dagger.hilt.android.scopes.ActivityRetainedScoped
import kotlinx.coroutines.flow.MutableSharedFlow
import javax.inject.Inject

@ActivityRetainedScoped
class NavigatorImpl @Inject constructor(
    private val encoder: Base64Encoder,
) : Navigator {
    override val sharedFlow: MutableSharedFlow<String> = MutableSharedFlow()
    private val moshi by lazy {
        Moshi.Builder().addLast(KotlinJsonAdapterFactory()).build()
    }

    override suspend fun navigateToSeries(seriesGroup: SeriesGroup) =
        sharedFlow.emit(
            Screen.SeriesGroupDetailScreen.route.replace(
                "{series_group_data}",
                encoder.encode(moshi.adapter(SeriesGroup::class.java).toJson(seriesGroup)).also {
                    val x = encoder.decode(it)
                    Log.d("Indivar", x)
                }
            )
        )

}
