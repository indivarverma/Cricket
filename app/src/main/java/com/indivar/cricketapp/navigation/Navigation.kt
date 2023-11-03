package com.indivar.cricketapp.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import androidx.navigation.navigation
import com.indivar.core.Navigator
import com.indivar.cricketapp.ui.fixtures.list.SeriesFixturesListView
import com.indivar.cricketapp.ui.match.detail.ui.MatchDetailViewScreen
import com.indivar.cricketapp.ui.series.detail.SeriesDetailViewScreen
import com.indivar.cricketapp.ui.series.groups.ui.SeriesGroupsViewScreen
import com.indivar.cricketapp.utils.activity
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

sealed class Screen(val route: String) {

    object MatchDetailScreen : Screen(route = "match_detail/{matchId}")
    object SeriesGroupsScreen : Screen(route = "series_list")
    object SeriesGroupDetailScreen : Screen(route = "series_group/{series_group_data}")
    object SeriesFixturesListScreen : Screen(route = "series_fixtures/{series_id}")
    object StartScreen : Screen(route = "start")
}


@Composable
fun Navigation(navigator: Navigator) {
    val navigationController = rememberNavController()
    val activity by rememberUpdatedState(newValue = LocalContext.current)
    LaunchedEffect(key1 = "Navigation") {
        navigator.sharedFlow.onEach {
            navigationController.navigate(it) {
                popUpTo(it)
            }
        }.launchIn(this)
    }
    NavHost(
        navController = navigationController,
        startDestination = Screen.StartScreen.route
    ) {


        SeriesGraph()
        composable(
            route = Screen.MatchDetailScreen.route,
            arguments = listOf(
                navArgument("matchId") {
                    type = NavType.IntType
                    defaultValue = 2768819
                }
            )
        ) { entry ->

            MatchDetailViewScreen(
                matchDetailViewModel = hiltViewModel(),
                onPlayerSelected = { playerId ->
                    navigationController.navigate("player_details/${playerId}")
                },
                finish = {
                    if (!navigationController.popBackStack()) {
                        activity.activity?.finish()
                    }
                },
            )
        }
    }
}

fun NavGraphBuilder.SeriesGraph() {
    navigation(
        startDestination = Screen.SeriesGroupsScreen.route,
        route = Screen.StartScreen.route
    ) {
        composable(
            route = Screen.SeriesGroupsScreen.route,

            ) {

            SeriesGroupsViewScreen(
                viewModel = hiltViewModel(),
            )

        }
        composable(
            route = Screen.SeriesGroupDetailScreen.route,
            arguments = listOf(
                navArgument("series_group_data") {
                    type = NavType.StringType
                }
            ),

            ) {

            SeriesDetailViewScreen(
                hiltViewModel()
            )

        }
        composable(
            route = Screen.SeriesFixturesListScreen.route,
            arguments = listOf(
                navArgument("series_id") {
                    type = NavType.IntType
                }
            ),

            ) {
                SeriesFixturesListView(
                    hiltViewModel()
                )

        }
    }
}


