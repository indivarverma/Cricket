package com.indivar.core.common.domain.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

abstract class MviViewModel<ViewState, DataState, Effect>() : ViewModel() {
    private val _state = MutableStateFlow(initialState)
    private val _effects = MutableSharedFlow<Effect>()
    val state: Flow<ViewState> = _state.map(::mapState)
    val effects: SharedFlow<Effect> = _effects.asSharedFlow()

    abstract val initialState: DataState

    val initialViewState = mapState(initialState)
    abstract fun mapState(dataState: DataState): ViewState

    fun enqueue(reduce: DataState.() -> DataState) {
        _state.update {
            it.reduce()
        }
    }

    fun queueEffects(list: List<Effect>) {
        viewModelScope.launch {
            list.forEach{_effects.emit(it)}
        }
    }

}