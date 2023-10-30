package com.indivar.core.viewmodels

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.indivar.core.Response
import com.indivar.models.Match
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.format.DateTimeFormatter
import javax.inject.Inject

@HiltViewModel
class MatchDetailViewModel
@Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val pullMatchDetailsUseCase: PullMatchDetailsUseCase,
) : ViewModel() {
    private val matchId = checkNotNull(savedStateHandle.get<Int>("matchId"))
    private val _state =
        MutableStateFlow(MatchDetailState(matchId = null, data = PullState.Loading))
    private val _effects = MutableSharedFlow<List<MatchDetailsEffect>>()

    init {
        fetch(matchId)
    }

    val viewState: Flow<MatchViewState> = _state.map(::mapState)
    val effects: SharedFlow<List<MatchDetailsEffect>> = _effects.asSharedFlow()

    private suspend fun fetch() {
        fetch(matchId)
    }


    private fun fetch(id: Int, onSuccess: () -> Unit = {}) {
        viewModelScope.launch {
            val matchDetail = pullMatchDetailsUseCase.trigger(id)
            _state.value = _state.value.copy(
                matchId = id,
                data = when (matchDetail) {
                    is Response.Success -> PullState.Pulled(matchDetail.data).also {
                        onSuccess()
                    }

                    is Response.Error -> PullState.Failed
                }
            )
        }
    }

    private fun mapState(state: MatchDetailState): MatchViewState =
        when (val data = state.data) {
            is PullState.Pulled -> MatchViewState(
                showLoading = false,
                showError = false,
                info = MatchInformation(
                    startDate = data.detail.matchDates?.start?.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME)
                        ?: "",
                    endDate = data.detail.matchDates?.end?.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME)
                        ?: "",
                    title = data.detail.title,
                    homeTeam = data.detail.homeTeam?.name ?: "",
                    awayTeam = data.detail.awayTeams.getOrNull(0)?.name ?: "",
                    result = data.detail.result,
                    tossResult = data.detail.tossResult,
                    homeTeamScores = data.detail.homeTeamScores,
                    awayTeamScores = data.detail.awayTeamScores,
                    firstEmpire = data.detail.matchOfficials?.firstUmpire,
                    secondEmpire = data.detail.matchOfficials?.secondUmpire,
                    thirdEmpire = data.detail.matchOfficials?.thirdUmpire,
                    scoreCard = data.detail.scoreCard,
                ),
                refetch = ::fetch
            )

            is PullState.Failed -> MatchViewState(
                showLoading = false,
                showError = true,
                info = null,
                refetch = ::fetch
            )

            is PullState.Loading -> MatchViewState(
                showLoading = true,
                showError = false,
                info = null,
                refetch = ::fetch
            )
        }


    data class MatchDetailState(
        val matchId: Int?,
        val data: PullState,
    )

    sealed class PullState {
        object Loading : PullState()
        data class Pulled(val detail: Match) : PullState()

        object Failed : PullState()
    }
}