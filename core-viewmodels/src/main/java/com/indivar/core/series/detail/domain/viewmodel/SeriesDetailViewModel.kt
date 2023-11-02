package com.indivar.core.series.detail.domain.viewmodel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.indivar.core.Navigator
import com.indivar.core.common.domain.viewmodel.MviViewModel
import com.indivar.core.series.detail.domain.usecase.GetSeriesGroupUseCase
import com.indivar.models.series.Series
import com.indivar.models.series.SeriesGroup
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class SeriesDetailViewModel @Inject constructor(
    private val navigator: Navigator,
    private val stateHandle: SavedStateHandle,
    private val useCase: GetSeriesGroupUseCase,
) : MviViewModel<SeriesDetailViewState, SeriesDetailViewModel.DataState, SeriesDetailEffect>() {

    private val groupData by lazy { requireNotNull(stateHandle.get<String>("series_group_data")) }

    init {
        viewModelScope.launch {
            val dSeriesGroup = async(Dispatchers.IO) {
                useCase.getSeriesGroup(groupData)
            }

            val seriesGroup = dSeriesGroup.await()
            enqueue {
                copy(seriesGroup = seriesGroup)
            }

        }
    }

    private fun onSeriesSelected(series: Series) {
        viewModelScope.launch {
            navigator.navigateToSeriesFixtures(series)
        }
    }

    override val initialState: DataState
        get() = DataState(null)

    override fun mapState(state: DataState): SeriesDetailViewState = SeriesDetailViewState(
        title = state.seriesGroup?.type ?: "",
        list = state.seriesGroup?.series.orEmpty(),
        onSeriesSelected = ::onSeriesSelected,
    )


    data class DataState(
        val seriesGroup: SeriesGroup?,
    )
}