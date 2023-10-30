package com.indivar.cricketapp.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.indivar.cricketapp.ui.utils.activity


sealed class Screen(val route: String) {

    object MatchDetailScreen : Screen(route = "match_detail/{matchId}")
    object SeriesListScreen : Screen(route = "series_list")
}


@Composable
fun Navigation() {
    val navigationController = rememberNavController()
    val activity by rememberUpdatedState(newValue = LocalContext.current)
    NavHost(
        navController = navigationController,
        startDestination = Screen.SeriesListScreen.route
    ) {
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

        composable(
            route = Screen.SeriesListScreen.route,

            ) {

            SeriesListViewScreen(
                viewModel = hiltViewModel(),
            )

        }
    }
}


