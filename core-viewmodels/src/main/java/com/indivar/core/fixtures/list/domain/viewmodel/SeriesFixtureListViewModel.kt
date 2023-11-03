package com.indivar.core.fixtures.list.domain.viewmodel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.indivar.core.Navigator
import com.indivar.core.common.domain.viewmodel.MviViewModel
import com.indivar.core.data.Response
import com.indivar.core.fixtures.list.domain.usecase.PullSeriesFixturesUseCase
import com.indivar.models.series.Fixture
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SeriesFixtureListViewModel @Inject constructor(
    private val pullSeriesFixturesUseCase: PullSeriesFixturesUseCase,
    private val savedStateHandle: SavedStateHandle,
    private val navigator: Navigator,
) :
    MviViewModel<SeriesFixturesListViewState, SeriesFixtureListViewModel.SeriesFixturesListDataState, SeriesFixturesListEffect>() {

    private val seriesId by lazy {
        requireNotNull(savedStateHandle.get<Int>("series_id"))
    }

    data class SeriesFixturesListDataState(
        val fixtures: List<Fixture>,
        val isError: Boolean,
        val isLoading: Boolean,
    )

    override val initialState: SeriesFixturesListDataState
        get() = SeriesFixturesListDataState(
            fixtures = emptyList(),
            isError = false,
            isLoading = false,
        )

    init {
        fetch()
    }

    private fun fetch() {
        enqueue {
            copy(
                isLoading = true
            )
        }
        viewModelScope.launch {
            val seriesFixtures = pullSeriesFixturesUseCase.fetchSeriesFixtures(seriesId)

            enqueue {
                when (seriesFixtures) {
                    is Response.Success -> copy(
                        fixtures = seriesFixtures.data.fixtures,
                        isError = false,
                        isLoading = false,
                    )

                    is Response.Error -> copy(
                        isLoading = false,
                        isError = true,
                        fixtures = emptyList(),
                    )
                }
            }

        }

    }

    private fun onFixtureClicked(fixture: Fixture) {
        viewModelScope.launch {
            navigator.navigateToFixture(fixture)
        }
    }

    override fun mapState(dataState: SeriesFixturesListDataState): SeriesFixturesListViewState =
        SeriesFixturesListViewState(
            fixtures = dataState.fixtures,
            isError = dataState.isError,
            isLoading = dataState.isLoading,
            onFixtureClicked = ::onFixtureClicked,
        )
}

data class SeriesFixturesListViewState(
    val fixtures: List<Fixture>,
    val isError: Boolean,
    val isLoading: Boolean,
    val onFixtureClicked: (Fixture) -> Unit,
)

sealed class SeriesFixturesListEffect {

}

