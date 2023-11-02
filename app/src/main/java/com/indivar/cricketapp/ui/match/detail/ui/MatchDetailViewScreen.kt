package com.indivar.cricketapp.ui.match.detail.ui

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.indivar.core.match.detail.domain.viewmodel.MatchDetailViewModel
import com.indivar.core.match.detail.domain.viewmodel.MatchDetailsEffect
import com.indivar.core.match.detail.domain.viewmodel.MatchViewState
import com.indivar.cricketapp.collectAsEffect
import com.indivar.cricketapp.ui.common.LoadingScreen
import kotlinx.coroutines.Dispatchers

@Composable
fun MatchDetailViewScreen(
    matchDetailViewModel: MatchDetailViewModel,
    onPlayerSelected: (Int) -> Unit,
    finish: () -> Unit,
) {

    val finishRef by rememberUpdatedState(newValue = finish)

    var dialogState by remember {
        mutableStateOf(false)
    }
    val coroutineScope = rememberCoroutineScope()


    fun MatchDetailsEffect.consume(): Any =
        when (this) {
            is MatchDetailsEffect.ToastNow -> {
                dialogState = true
            }

            is MatchDetailsEffect.CheckPlayerDetails ->
                onPlayerSelected(playerId)

        }

    val state = matchDetailViewModel.state.collectAsState(MatchViewState.initial).value
    matchDetailViewModel.effects.collectAsEffect(
        coroutineScope = coroutineScope,
        context = Dispatchers.Main,
        block = MatchDetailsEffect::consume
    )

    if (state.showLoading) {
        LoadingScreen(Modifier.fillMaxSize())
    }
    if (state.showError) {
        Text("Encountered Error")
    }
    state.info?.let {
        LoadedScreen(it, onPlayerSelected)
    }

    if (dialogState) {
        AlertDialog(
            title = { Text(text = "Trial alert") },
            onDismissRequest = {
                dialogState = false
            }, confirmButton = {
                Button(onClick = {
                    finishRef()
                    dialogState = false
                }) {
                    Text(text = "Okay")
                }
            })
    }
    BackHandler {
        dialogState = true
    }
}


