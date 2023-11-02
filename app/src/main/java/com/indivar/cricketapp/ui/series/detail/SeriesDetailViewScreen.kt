package com.indivar.cricketapp.ui.series.detail

import android.annotation.SuppressLint
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.unit.dp
import com.indivar.core.series.detail.domain.viewmodel.SeriesDetailEffect
import com.indivar.core.series.detail.domain.viewmodel.SeriesDetailViewModel
import com.indivar.cricketapp.collectAsEffect
import com.indivar.models.series.Series
import kotlinx.coroutines.Dispatchers

@Composable
fun SeriesDetailViewScreen(
    viewModel: SeriesDetailViewModel
) {
    val coroutineScope = rememberCoroutineScope()

    fun SeriesDetailEffect.consume() = Unit

    val state = viewModel.state.collectAsState(viewModel.initialViewState).value
    viewModel.effects.collectAsEffect(
        coroutineScope = coroutineScope,
        context = Dispatchers.Main,
        block = SeriesDetailEffect::consume
    )

    SeriesDetailView(
        title = state.title,
        series = state.list,
        onSeriesSelected = state.onSeriesSelected,
    )

}

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SeriesDetailView(
    title: String,
    series: List<Series>,
    onSeriesSelected: Series.() -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(title = {
                Text(
                    text = title,
                    style = MaterialTheme.typography.headlineMedium
                )
            })
        }
    ) {
        LazyColumn(modifier = Modifier.padding(it)) {
            items(series) { item ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(100.dp)
                        .padding(8.dp)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .clickable {
                                item.onSeriesSelected()
                            }
                            .padding(horizontal = 24.dp, vertical = 12.dp),
                        verticalArrangement = Arrangement.SpaceEvenly,
                        horizontalAlignment = Alignment.Start,
                    ) {
                        Text(
                            text = item.name,
                            style = MaterialTheme.typography.titleMedium,
                        )
                        Row(verticalAlignment = Alignment.Bottom) {
                            Text(
                                text = item.season,
                                style = MaterialTheme.typography.bodyMedium,
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = item.status,
                                style = MaterialTheme.typography.labelMedium,
                                fontStyle = FontStyle.Italic
                            )
                        }
                    }
                }

            }
        }
    }
}