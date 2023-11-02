package com.indivar.cricketapp.ui.series.list.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.indivar.core.series.list.domain.viewmodel.SeriesListEffect
import com.indivar.core.series.list.domain.viewmodel.SeriesListViewModel
import com.indivar.core.series.list.domain.viewmodel.SeriesListViewState
import com.indivar.cricketapp.collectAsEffect
import com.indivar.cricketapp.ui.common.LoadingScreen
import com.indivar.models.series.AllSeries
import com.indivar.models.series.SeriesGroup
import kotlinx.coroutines.Dispatchers

@Composable
fun SeriesListViewScreen(
    viewModel: SeriesListViewModel,
) {
    var dialogState by remember {
        mutableStateOf(false)
    }

    val coroutineScope = rememberCoroutineScope()

    fun SeriesListEffect.consume() {
        when (this) {
            is SeriesListEffect.ToastNow -> {
                dialogState = true
            }
        }
    }

    val state = viewModel.state.collectAsState(SeriesListViewState.initial).value
    viewModel.effects.collectAsEffect(
        coroutineScope = coroutineScope,
        context = Dispatchers.Main,
        block = SeriesListEffect::consume
    )

    if (state.showLoading) {
        LoadingScreen(Modifier.fillMaxSize())
    }
    if (state.showError) {
        Text("Encountered Error")
    }
    state.allSeries?.let {
        AllSeriesView(it, state.onSeriesItemClicked)
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AllSeriesView(
    data: AllSeries,
    onItemClick: (SeriesGroup) -> Unit,
) {
    Scaffold(
        topBar = {
            TopAppBar(title = {
                Text(
                    text = data.title,
                    style = MaterialTheme.typography.headlineMedium
                )
            })
        },
    ) { paddingValues ->
        LazyColumn(modifier = Modifier.padding(paddingValues = paddingValues)) {

            item(data.description) {
                Text(
                    text = data.description,
                    style = MaterialTheme.typography.titleMedium
                )
            }
            items(items = data.seriesGroup, key = { it.type }) { seriesGroup ->

                Card(modifier = Modifier
                    .fillMaxWidth()
                    .height(60.dp)
                    .padding(8.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .clickable {
                                onItemClick.invoke(seriesGroup)
                            }
                            .padding(horizontal = 24.dp),
                        contentAlignment = Alignment.CenterStart
                    ) {
                        Text(
                            text = seriesGroup.type,
                            modifier = Modifier
                        )
                    }
                }

            }
        }
    }

}