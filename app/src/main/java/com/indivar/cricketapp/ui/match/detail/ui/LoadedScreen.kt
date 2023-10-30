package com.indivar.cricketapp.ui.match.detail.ui

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.sp
import com.indivar.core.match.detail.domain.viewmodel.MatchInformation

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