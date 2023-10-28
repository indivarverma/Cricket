package com.indivar.core.viewmodels

data class MatchViewState(
    val showLoading: Boolean,
    val showError: Boolean,
    val refetch: suspend () -> Unit,
) {
    companion object {
        val initial = MatchViewState(showLoading = true, showError = false, refetch = {})
    }
}