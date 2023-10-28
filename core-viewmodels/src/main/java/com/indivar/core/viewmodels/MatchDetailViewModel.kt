package com.indivar.core.viewmodels

import androidx.lifecycle.ViewModel
import com.indivar.models.Match
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class MatchDetailViewModel
@Inject constructor(private val pullMatchDetailsUseCase: PullMatchDetailsUseCase) : ViewModel() {
    private val _state = MutableStateFlow(MatchDetailState(matchId = 0, data = PullState.Loading))
    private val _effects = MutableSharedFlow<List<MatchDetailsEffect>>()

    val viewState: Flow<MatchViewState> = _state.map(::mapState)
    val effects: Flow<List<MatchDetailsEffect>> =
        _effects.onStart { emit(listOf(MatchDetailsEffect.Ready(::fetch))) }

    private suspend fun fetch() {
        _state.update { prevState ->
            val result = pullMatchDetailsUseCase.invoke(matchId = prevState.matchId)
            prevState.copy(data = result?.let { PullState.Pulled(result) } ?: PullState.Failed)

        }
    }

    private suspend fun fetch(id: Int, onSuccess: () -> Unit = {}) {

        val result = pullMatchDetailsUseCase.invoke(id)
        _state.update { prevState ->
            prevState.copy(matchId = id,
                data = result?.let { PullState.Pulled(result) } ?: PullState.Failed).also {
                onSuccess()

            }
        }

    }

    private fun mapState(state: MatchDetailState): MatchViewState =
        MatchViewState(showLoading = state.data is PullState.Loading, refetch = ::fetch)

    data class MatchDetailState(
        val matchId: Int,
        val data: PullState,
    )

    sealed class PullState {
        object Loading : PullState()
        data class Pulled(val detail: Match) : PullState()

        object Failed : PullState()
    }
}