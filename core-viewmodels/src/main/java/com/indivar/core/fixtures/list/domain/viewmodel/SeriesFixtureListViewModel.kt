package com.indivar.core.fixtures.list.domain.viewmodel

import com.indivar.core.common.domain.viewmodel.MviViewModel
import com.indivar.core.fixtures.list.domain.usecase.PullSeriesFixturesUseCase
import com.indivar.models.match.Match
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SeriesFixtureListViewModel @Inject constructor(
    private val pullSeriesFixturesUseCase: PullSeriesFixturesUseCase,
) :
    MviViewModel<SeriesFixturesListViewState, SeriesFixtureListViewModel.SeriesFixturesListDataState, SeriesFixturesListEffect>() {

    data class SeriesFixturesListDataState(
        val fixtures: List<Match>
    )

    override val initialState: SeriesFixturesListDataState
        get() = SeriesFixturesListDataState(emptyList())

    override fun mapState(dataState: SeriesFixturesListDataState): SeriesFixturesListViewState =
        SeriesFixturesListViewState(
            dataState.fixtures
        )
}

data class SeriesFixturesListViewState(
    val fixtures: List<Match>,
)

sealed class SeriesFixturesListEffect {

}

