package com.indivar.core.series.groups.domain.viewmodel


import androidx.lifecycle.viewModelScope
import com.indivar.core.Navigator
import com.indivar.core.common.domain.viewmodel.MviViewModel
import com.indivar.core.data.Response
import com.indivar.core.series.groups.domain.usecase.PullSeriesGroupsUseCase
import com.indivar.models.series.SeriesGroups
import com.indivar.models.series.SeriesGroup
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SeriesGroupsViewModel
@Inject constructor(
    private val useCase: PullSeriesGroupsUseCase,
    private val navigator: Navigator,
) : MviViewModel<SeriesGroupsViewState, SeriesGroupsViewModel.SeriesGroupsDataState, SeriesGroupsEffect>() {

    override val initialState: SeriesGroupsDataState
        get() = SeriesGroupsDataState(data = PullState.Loading)


    init {
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

    override fun mapState(state: SeriesGroupsDataState): SeriesGroupsViewState =
        when (val data = state.data) {
            is PullState.Pulled -> SeriesGroupsViewState(
                seriesGroups = data.seriesGroups,
                showError = false,
                showLoading = false,
                refetch = ::fetch,
                onSeriesItemClicked = ::onSeriesItemClicked,
            )

            is PullState.Failed -> SeriesGroupsViewState(
                seriesGroups = null,
                showError = true,
                showLoading = false,
                onSeriesItemClicked = ::onSeriesItemClicked,
                refetch = ::fetch,
            )

            is PullState.Loading -> SeriesGroupsViewState(
                showLoading = true,
                showError = false,
                seriesGroups = null,
                onSeriesItemClicked = ::onSeriesItemClicked,
                refetch = ::fetch
            )
        }


    data class SeriesGroupsDataState(
        val data: PullState,
    )

    sealed class PullState {
        object Loading : PullState()
        data class Pulled(val seriesGroups: SeriesGroups) : PullState()

        object Failed : PullState()
    }
}