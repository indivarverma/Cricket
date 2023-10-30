package com.indivar.cricketapp.ui

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.indivar.core.viewmodels.MatchDetailViewModel
import com.indivar.core.viewmodels.MatchDetailsEffect
import com.indivar.core.viewmodels.MatchInformation
import com.indivar.core.viewmodels.MatchViewState
import com.indivar.cricketapp.collectAsEffect
import com.indivar.models.BattingMatchStat
import com.indivar.models.Player
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

    val state = matchDetailViewModel.viewState.collectAsState(MatchViewState.initial).value
    matchDetailViewModel.effects.collectAsEffect(
        coroutineScope = coroutineScope,
        context = Dispatchers.Main,
        block = {
            it.forEach(MatchDetailsEffect::consume)
        })

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




@Composable
fun LoadedScreen(
    information: MatchInformation,
    onPlayerSelected: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn {
        information.title
        item(information.title) {
            Text(text = information.title, fontFamily = FontFamily.Serif, fontSize = 15.sp)
        }
        item {
            Text(
                text = "${information.homeTeam} - ${information.homeTeamScores.orEmpty()}",
                fontFamily = FontFamily.Serif,
                fontSize = 15.sp
            )
        }
        item {
            Text(
                text = "${information.awayTeam} - ${information.awayTeamScores.orEmpty()}",
                fontFamily = FontFamily.Serif,
                fontSize = 15.sp
            )
        }
        information.tossResult?.let {
            item {
                Text(
                    text = "${information.tossResult}",
                    fontFamily = FontFamily.Serif,
                    fontSize = 15.sp
                )
            }
        }
        item {
            Text(text = "${information.result}", fontFamily = FontFamily.Serif, fontSize = 15.sp)
        }
        information.scoreCard?.innings?.firstOrNull()?.let {
            items(it.batting, { (player, _) -> player.id }) { (player, matchStat) ->
                ScoreRow(player, matchStat, onPlayerSelected, Modifier.fillMaxWidth())
            }
        }
    }

}

@Composable
fun ScoreRow(
    player: Player,
    matchStat: BattingMatchStat,
    onPlayerSelected: (Int) -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier
            .padding(16.dp)
            .wrapContentHeight()
            .clickable {
                onPlayerSelected.invoke(player.id)
            },
        horizontalArrangement = Arrangement.SpaceAround,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(text = "${matchStat.battingOrder}")
        Column {
            Text(text = player.name)
            Text(text = matchStat.outStyle.orEmpty())
            if (matchStat.balls > 0) {
                Text(text = "${matchStat.runs} runs /${matchStat.balls} balls")
            }
            Text(text = matchStat.minutesOfPlay?.let { "$it minutes" } ?: "Yet to bat")
        }
        Text(text = matchStat.strikeRate?.let { "S/R $it" }.orEmpty())
    }
}