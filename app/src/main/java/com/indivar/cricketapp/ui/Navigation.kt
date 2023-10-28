package com.indivar.cricketapp.ui

import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument


sealed class Screen(val route: String) {

    object MatchDetailScreen : Screen(route = "match_detail/{matchId}")
    object PlayerDetailScreen : Screen(route = "player_details/{playerId}")
}


@Composable
fun Navigation() {
    val navigationController = rememberNavController()
    NavHost(
        navController = navigationController,
        startDestination = Screen.MatchDetailScreen.route
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
            entry.arguments?.getInt("matchId")?.let { matchId ->
                MatchDetailViewScreen(navigationController, matchId)
            }
        }

        composable(
            route = Screen.PlayerDetailScreen.route,
            arguments = listOf(
                navArgument("playerId") {
                    type = NavType.IntType
                }
            )
        ) {

            it.arguments?.getInt("playerId")?.let { playerId ->
                PlayerDetailViewScreen(navigationController, playerId)
            }

        }
    }
}


