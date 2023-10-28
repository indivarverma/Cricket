package com.indivar.core.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.indivar.core.Response
import com.indivar.models.Match
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlinx.coroutines.withTimeoutOrNull
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
            prevState.copy(
                data = when (result) {
                    is Response.Success -> PullState.Pulled(result.data)
                    is Response.Error -> PullState.Failed
                }
            )

        }
    }

    private fun fetch(id: Int, onSuccess: () -> Unit = {}) {
        viewModelScope.launch {
            val result = withTimeoutOrNull(5000) {
                withContext(Dispatchers.IO) {
                    pullMatchDetailsUseCase.invoke(id)
                }
            } ?: Response.Error(errorCode = 404, errorMessage = "Timed Out")
            _state.value = _state.value.copy(
                matchId = id,
                data = when (result) {
                    is Response.Success -> PullState.Pulled(result.data).also {
                        onSuccess()
                    }
                    is Response.Error -> PullState.Failed
                }
            )
        }
    }

    private fun onClick() {
        _state.update {
            it
        }
    }

    private fun mapState(state: MatchDetailState): MatchViewState =
        MatchViewState(
            showLoading = state.data is PullState.Loading,
            showError = state.data is PullState.Failed,
            refetch = ::fetch
        )

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