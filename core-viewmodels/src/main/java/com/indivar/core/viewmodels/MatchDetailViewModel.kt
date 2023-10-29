package com.indivar.core.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.indivar.core.Response
import com.indivar.models.Match
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.format.DateTimeFormatter
import javax.inject.Inject

@HiltViewModel
class MatchDetailViewModel
@Inject constructor(private val pullMatchDetailsUseCase: PullMatchDetailsUseCase) : ViewModel() {
    private val _state =
        MutableStateFlow(MatchDetailState(matchId = null, data = PullState.Loading))
    private val _effects = MutableSharedFlow<List<MatchDetailsEffect>>()

    val viewState: Flow<MatchViewState> = _state.map(::mapState)
    val effects: Flow<List<MatchDetailsEffect>> =
        _effects.onStart { emit(listOf(MatchDetailsEffect.Ready(::fetch))) }

    private suspend fun fetch() {
        _state.update { prevState ->
            val result = prevState.matchId?.let { pullMatchDetailsUseCase.trigger(matchId = it) }
            prevState.copy(
                data = when (result) {
                    is Response.Success -> PullState.Pulled(result.data)
                    is Response.Error -> PullState.Failed
                    null -> PullState.Failed
                }
            )

        }
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