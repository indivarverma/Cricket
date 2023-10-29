package com.indivar.cricketapp.ui

import android.app.Activity
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.navigation.NavType
import androidx.navigation.activity
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.indivar.cricketapp.ui.utils.activity


sealed class Screen(val route: String) {

    object MatchDetailScreen : Screen(route = "match_detail/{matchId}")
    object PlayerDetailScreen : Screen(route = "player_details/{playerId}")
}


@Composable
fun Navigation() {
    val navigationController = rememberNavController()
    val activity by rememberUpdatedState(newValue = LocalContext.current)
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
                MatchDetailViewScreen(matchId,  { playerId ->
                    navigationController.navigate("player_details/${playerId}")
                }){
                  if(!navigationController.popBackStack()) {
                      activity.activity?.finish()
                  }
                }
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


