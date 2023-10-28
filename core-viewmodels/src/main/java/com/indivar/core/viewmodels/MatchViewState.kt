package com.indivar.core.viewmodels

data class MatchViewState(
    val showLoading: Boolean,
    val refetch: () -> Unit
) {
    companion object {
        val initial = MatchViewState(
            showLoading = true,
            refetch = {}
        )
    }
}