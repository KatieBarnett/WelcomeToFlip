package dev.katiebarnett.welcometoflip

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import dev.katiebarnett.welcometoflip.core.models.mapToGameType
import dev.katiebarnett.welcometoflip.screens.ChooseGameScreen
import dev.katiebarnett.welcometoflip.screens.RegularGameScreen
import dev.katiebarnett.welcometoflip.screens.SoloGameScreen

@Composable
fun WelcomeToFlipNavHost(
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    NavHost(
        navController = navController,
        startDestination = ChooseGame.route,
        modifier = modifier
    ) {
        composable(route = ChooseGame.route) {
            ChooseGameScreen(
                chooseGameAction = {
                    navController.navigate(route = Game.getRoute(it))
                },
                loadGameAction = {
                    navController.navigate(route = Game.getRoute(it)) 
                }
            )
        }
        composable(route = Game.routeWithArgs, arguments = Game.arguments, deepLinks = Game.deepLinks) {
            // TODO handle navigation errors
            it.arguments?.getString(Game.gameTypeArg)?.mapToGameType()?.let { gameType ->
                val seed = it.arguments?.getString(Game.seedArg)?.toLong()
                val position = it.arguments?.getString(Game.positionArg)?.toInt()
                if (gameType.solo) {
                    SoloGameScreen(
                        viewModel = hiltViewModel(),
                        gameType = gameType,
                        seed = seed,
                        initialPosition = position,
                        onGameEnd = { navController.navigateUp() }
                    )
                } else {
                    RegularGameScreen(
                        viewModel = hiltViewModel(),
                        gameType = gameType,
                        seed = seed,
                        initialPosition = position,
                        onGameEnd = { navController.navigateUp() }
                    )
                }
            }
        }
    }
}