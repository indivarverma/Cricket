package com.indivar.core.match.detail.domain.viewmodel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.indivar.core.common.domain.viewmodel.MviViewModel
import com.indivar.core.data.Response
import com.indivar.core.match.detail.domain.usecase.PullMatchDetailsUseCase
import com.indivar.models.match.Match
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.time.format.DateTimeFormatter
import javax.inject.Inject

@HiltViewModel
class MatchDetailViewModel
@Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val pullMatchDetailsUseCase: PullMatchDetailsUseCase,
) : MviViewModel<MatchViewState, MatchDetailViewModel.MatchDetailState, MatchDetailsEffect>() {
    private val matchId = checkNotNull(savedStateHandle.get<Int>("matchId"))

    init {
        fetch(matchId)
    }

    override val initialState: MatchDetailState
        get() = MatchDetailState(matchId = null, data = PullState.Loading)

    private fun fetch() {
        fetch(matchId)
    }


    private fun fetch(id: Int, onSuccess: () -> Unit = {}) {
        viewModelScope.launch {
            val matchDetail = pullMatchDetailsUseCase.trigger(id)
            enqueue {
                copy(
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
    }

    override fun mapState(state: MatchDetailState): MatchViewState =
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