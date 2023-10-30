package com.indivar.cricketapp.ui.series.list.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
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

    val state = viewModel.viewState.collectAsState(SeriesListViewState.initial).value
    viewModel.effects.collectAsEffect(
        coroutineScope = coroutineScope,
        context = Dispatchers.Main,
        block = {
            it.forEach(SeriesListEffect::consume)
        })

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

@Composable
fun AllSeriesView(
    data: AllSeries,
    onItemClick: (SeriesGroup) -> Unit,
) {
    LazyColumn {
        item(data.title) {
            Text(
                text = data.title,
                style = MaterialTheme.typography.headlineMedium
            )
        }
        item(data.description) {
            Text(
                text = data.description,
                style = MaterialTheme.typography.titleMedium
            )
        }
        items(items = data.seriesGroup, key = { it.type }) { seriesGroup ->
            Text(
                text = seriesGroup.type,
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.clickable {
                    onItemClick.invoke(seriesGroup)
                }
            )
        }
    }
}