package com.indivar.core.series.list.domain.viewmodel


import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.indivar.core.Navigator
import com.indivar.core.data.Response
import com.indivar.core.series.list.domain.usecase.PullAllSeriesUseCase
import com.indivar.models.series.AllSeries
import com.indivar.models.series.SeriesGroup
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SeriesListViewModel
@Inject constructor(
    private val useCase: PullAllSeriesUseCase,
    private val navigator: Navigator,
) : ViewModel() {

    private val _state =
        MutableStateFlow(SeriesListDataState(data = PullState.Loading))
    private val _effects = MutableSharedFlow<List<SeriesListEffect>>()

    init {
        fetch()
    }

    val viewState: Flow<SeriesListViewState> = _state.map(::mapState)
    val effects: SharedFlow<List<SeriesListEffect>> = _effects.asSharedFlow()

    private suspend fun reFetch() {
        fetch()
    }


    private fun fetch(onSuccess: () -> Unit = {}) {
        viewModelScope.launch {
            val allSeriesResponse = useCase.trigger()
            _state.value = _state.value.copy(
                data = when (allSeriesResponse) {
                    is Response.Success -> PullState.Pulled(allSeriesResponse.data).also {
                        onSuccess()
                    }

                    is Response.Error -> PullState.Failed
                }
            )
        }
    }

    private fun onSeriesItemClicked(seriesGroup: SeriesGroup) {
        viewModelScope.launch {
            navigator.navigateToSeries(seriesGroup)
        }
    }

    private fun mapState(state: SeriesListDataState): SeriesListViewState =
        when (val data = state.data) {
            is PullState.Pulled -> SeriesListViewState(
                allSeries = data.allSeries,
                showError = false,
                showLoading = false,
                refetch = ::reFetch,
                onSeriesItemClicked = ::onSeriesItemClicked,
            )

            is PullState.Failed -> SeriesListViewState(
                allSeries = null,
                showError = true,
                showLoading = false,
                onSeriesItemClicked = ::onSeriesItemClicked,
                refetch = ::reFetch,
            )

            is PullState.Loading -> SeriesListViewState(
                showLoading = true,
                showError = false,
                allSeries = null,
                onSeriesItemClicked = ::onSeriesItemClicked,
                refetch = ::reFetch
            )
        }


    data class SeriesListDataState(
        val data: PullState,
    )

    sealed class PullState {
        object Loading : PullState()
        data class Pulled(val allSeries: AllSeries) : PullState()

        object Failed : PullState()
    }
}