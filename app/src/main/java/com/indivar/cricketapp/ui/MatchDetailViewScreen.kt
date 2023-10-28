package com.indivar.cricketapp.ui

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.indivar.core.viewmodels.MatchDetailViewModel
import com.indivar.core.viewmodels.MatchDetailsEffect
import com.indivar.core.viewmodels.MatchViewState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.EmptyCoroutineContext

@Composable
fun MatchDetailViewScreen(navigationController: NavHostController, matchId: Int) {
    var dialogState by remember {
        mutableStateOf(false)
    }


    @Composable
    fun <T> Flow<T>.collectAsEffect(
        context: CoroutineContext = EmptyCoroutineContext,
        block: (T) -> Unit
    ) {
        LaunchedEffect(key1 = Unit) {
            onEach(block).flowOn(context).launchIn(this)
        }
    }

    fun MatchDetailsEffect.consume() {
        when (this) {
            is MatchDetailsEffect.ToastNow -> {
                dialogState = true
            }

            is MatchDetailsEffect.CheckPlayerDetails ->
                navigationController.navigate(Screen.PlayerDetailScreen.route)

            is MatchDetailsEffect.Ready -> {
                this.triggerFetch(matchId)
            }

            else -> {

            }
        }

    }


    val matchDetailViewModel: MatchDetailViewModel = hiltViewModel()
    val state = matchDetailViewModel.viewState.collectAsState(MatchViewState.initial).value
    matchDetailViewModel.effects.collectAsEffect(context = Dispatchers.Main, block = {
        it.forEach(MatchDetailsEffect::consume)
    })

    /*LaunchedEffect(key1 = matchDetailViewModel.effects) {
        matchDetailViewModel.effects.collect {
            it.forEach(MatchDetailsEffect::consume)
        }
    }*/
    if (state.showLoading) {
        Text("Android Loading")
    } else {
        Text("Android Loaded")
    }
    if (dialogState) {
        AlertDialog(
            title = { Text(text = "Trial alert") },
            onDismissRequest = {
                dialogState = false
            }, confirmButton = {
                Button(onClick = { dialogState = false }) {
                    Text(text = "Okay")
                }
            })
    }
}

