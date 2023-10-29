package com.indivar.cricketapp.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalLifecycleOwner
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
    val matchDetailViewModel: MatchDetailViewModel = hiltViewModel()
    var dialogState by remember {
        mutableStateOf(false)
    }

    val coroutineScope = rememberCoroutineScope()

    @Composable
    fun <T> Flow<T>.collectAsEffect(
        context: CoroutineContext = EmptyCoroutineContext,
        block: (T) -> Unit
    ) {
        DisposableEffect(key1 = matchDetailViewModel.effects, key2 = LocalLifecycleOwner.current) {
            val job = onEach(block).flowOn(context).launchIn(coroutineScope)
            onDispose { job.cancel() }
        }
    }

    fun MatchDetailsEffect.consume(): Any =
        when (this) {
            is MatchDetailsEffect.ToastNow -> {
                dialogState = true
            }

            is MatchDetailsEffect.CheckPlayerDetails ->
                navigationController.navigate(Screen.PlayerDetailScreen.route)

            is MatchDetailsEffect.Ready -> {
                this@consume.triggerFetch(matchId)
            }


        }


    val state = matchDetailViewModel.viewState.collectAsState(MatchViewState.initial).value
    matchDetailViewModel.effects.collectAsEffect(context = Dispatchers.Main, block = {
        it.forEach(MatchDetailsEffect::consume)
    })

    if (state.showLoading) {
        LoadingScreen(Modifier.fillMaxSize())
    } else if (state.showError) {
        Text("Encountered Error")
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

@Composable
fun LoadingScreen(
    modifier: Modifier = Modifier,
) {
    Box(modifier = modifier, contentAlignment = Alignment.Center) {
        CircularProgressIndicator()
    }
}

