package com.indivar.core.series.groups.domain.viewmodel


import androidx.lifecycle.viewModelScope
import com.indivar.core.Navigator
import com.indivar.core.common.domain.viewmodel.MviViewModel
import com.indivar.core.data.Response
import com.indivar.core.series.groups.domain.usecase.PullSeriesGroupsUseCase
import com.indivar.models.series.SeriesGroup
import com.indivar.models.series.SeriesGroups
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
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
        viewModelScope.launch(CoroutineExceptionHandler { _, throwable ->
            enqueue {
                copy(
                    data = PullState.Failed(1002, throwable.message ?: "Unknown")
                )
            }
        }) {
            val allSeriesResponse = useCase.trigger()
            enqueue {
                copy(
                    data = when (allSeriesResponse) {
                        is Response.Success -> PullState.Pulled(allSeriesResponse.data).also {
                            onSuccess()
                        }

                        is Response.Error -> PullState.Failed(
                            code = allSeriesResponse.errorCode,
                            message = allSeriesResponse.errorMessage
                        )
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
                error = null,
                showLoading = false,
                refetch = ::fetch,
                onSeriesItemClicked = ::onSeriesItemClicked,
            )

            is PullState.Failed -> SeriesGroupsViewState(
                seriesGroups = null,
                error = ErrorState(code = data.code, message = data.message),
                showLoading = false,
                onSeriesItemClicked = ::onSeriesItemClicked,
                refetch = ::fetch,
            )

            is PullState.Loading -> SeriesGroupsViewState(
                showLoading = true,
                error = null,
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

        data class Failed(
            val code: Int,
            val message: String,
        ) : PullState()
    }
}