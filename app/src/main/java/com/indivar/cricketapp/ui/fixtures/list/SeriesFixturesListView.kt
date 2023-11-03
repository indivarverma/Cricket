package com.indivar.cricketapp.ui.fixtures.list

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
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
import androidx.compose.ui.unit.dp
import com.indivar.core.fixtures.list.domain.viewmodel.SeriesFixtureListViewModel
import com.indivar.core.fixtures.list.domain.viewmodel.SeriesFixturesListEffect
import com.indivar.cricketapp.collectAsEffect
import com.indivar.cricketapp.ui.common.LoadingScreen
import com.indivar.models.series.Fixture
import kotlinx.coroutines.Dispatchers
import java.time.format.DateTimeFormatter

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SeriesFixturesListView(
    viewModel: SeriesFixtureListViewModel
) {

    val coroutineScope = rememberCoroutineScope()
    fun SeriesFixturesListEffect.consume() = Unit

    val state = viewModel.state.collectAsState(viewModel.initialViewState).value
    viewModel.effects.collectAsEffect(
        coroutineScope = coroutineScope,
        context = Dispatchers.Main,
        block = SeriesFixturesListEffect::consume
    )

    Scaffold(
        topBar = {
            TopAppBar(title = {
                Text(
                    text = "Fixtures",
                    style = MaterialTheme.typography.headlineMedium
                )
            })
        },
    ) { paddingValues ->
        if (state.isLoading) {
            LoadingScreen(Modifier.fillMaxSize())
        }
        if (state.isError) {
            Text("Encountered Error")
        }
        FixturesListView(
            data = state.fixtures,
            onItemClick = state.onFixtureClicked,
            modifier = Modifier.padding(paddingValues)
        )
    }


}

@Composable
fun FixturesListView(
    data: List<Fixture>,
    onItemClick: (Fixture) -> Unit,
    modifier: Modifier = Modifier
) {

    LazyColumn(modifier = modifier) {


        items(items = data, key = { it.id }) { fixture ->

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight()
                    .padding(8.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .clickable {
                            onItemClick.invoke(fixture)
                        }
                        .padding(horizontal = 24.dp, vertical = 12.dp),
                    horizontalAlignment = Alignment.Start
                ) {
                    fixture.date?.let { date ->
                        Text(
                            text = DateTimeFormatter.ofPattern("MMMM dd, yyyy")
                                .format(date),
                            style = MaterialTheme.typography.bodySmall,
                        )
                    }

                    Text(
                        text = fixture.title,
                        style = MaterialTheme.typography.titleMedium,
                        maxLines = 2
                    )
                    Text(
                        text = fixture.subtitle,
                        style = MaterialTheme.typography.bodyMedium,
                        maxLines = 2
                    )
                    Text(
                        text = fixture.venue,
                        style = MaterialTheme.typography.bodySmall,
                    )
                    Row(
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = fixture.home.name,
                            style = MaterialTheme.typography.bodySmall,
                        )
                        Text(
                            text = "VS",
                            style = MaterialTheme.typography.bodySmall,
                        )
                        Text(
                            text = fixture.away.name,
                            style = MaterialTheme.typography.bodySmall,
                        )
                    }
                    Row(
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = fixture.status,
                            style = MaterialTheme.typography.bodySmall,
                        )
                        Text(
                            text = fixture.result,
                            style = MaterialTheme.typography.bodySmall,
                        )
                    }

                }
            }

        }
    }

}