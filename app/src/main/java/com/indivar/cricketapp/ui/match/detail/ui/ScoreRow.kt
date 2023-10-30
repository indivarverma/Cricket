package com.indivar.cricketapp.ui.match.detail.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.indivar.models.Player
import com.indivar.models.match.BattingMatchStat

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