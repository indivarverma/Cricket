package com.indivar.core.series.list.domain.viewmodel


import androidx.lifecycle.viewModelScope
import com.indivar.core.Navigator
import com.indivar.core.common.domain.viewmodel.MviViewModel
import com.indivar.core.data.Response
import com.indivar.core.series.list.domain.usecase.PullAllSeriesUseCase
import com.indivar.models.series.AllSeries
import com.indivar.models.series.SeriesGroup
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SeriesListViewModel
@Inject constructor(
    private val useCase: PullAllSeriesUseCase,
    private val navigator: Navigator,
) : MviViewModel<SeriesListViewState, SeriesListViewModel.SeriesListDataState, SeriesListEffect>() {

    override val initialState: SeriesListDataState
        get() = SeriesListDataState(data = PullState.Loading)


    init {
        fetch()
    }


    private suspend fun reFetch() {
        fetch()
    }


    private fun fetch(onSuccess: () -> Unit = {}) {
        viewModelScope.launch {
            val allSeriesResponse = useCase.trigger()
            enqueue {
                copy(
                    data = when (allSeriesResponse) {
                        is Response.Success -> PullState.Pulled(allSeriesResponse.data).also {
                            onSuccess()
                        }

                        is Response.Error -> PullState.Failed
                    }
                )
            }

        }
    }

    private fun onSeriesItemClicked(seriesGroup: SeriesGroup) {
        viewModelScope.launch {
            navigator.navigateToSeries(seriesGroup)
        }
    }

    override fun mapState(state: SeriesListDataState): SeriesListViewState =
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