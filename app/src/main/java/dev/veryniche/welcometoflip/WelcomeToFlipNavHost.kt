package dev.veryniche.welcometoflip

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import dev.veryniche.welcometoflip.core.models.mapToGameType
import dev.veryniche.welcometoflip.screens.AboutScreen
import dev.veryniche.welcometoflip.screens.ChooseGameScreen
import dev.veryniche.welcometoflip.screens.RegularGameScreen
import dev.veryniche.welcometoflip.screens.SoloGameScreen

@Composable
fun WelcomeToFlipNavHost(
    navController: NavHostController,
    modifier: Modifier = Modifier,
    mainViewModel: MainViewModel
) {
    NavHost(
        navController = navController,
        startDestination = ChooseGame.route,
        modifier = modifier
    ) {
        composable(route = ChooseGame.route) {
            ChooseGameScreen(
                navController = navController,
                viewModel = mainViewModel
            )
        }
        composable(route = About.route) {
            AboutScreen(
                navController = navController
            )
        }
        composable(route = Game.routeWithArgs, arguments = Game.arguments, deepLinks = Game.deepLinks) {
            // TODO handle navigation errors
            it.arguments?.getString(Game.gameTypeArg)?.mapToGameType()?.let { gameType ->
                val seed = it.arguments?.getString(Game.seedArg)?.toLong() ?: System.currentTimeMillis()
                val position = it.arguments?.getString(Game.positionArg)?.toInt() ?: 0

                if (gameType.solo) {
                    val soloGameViewModel =
                        hiltViewModel<SoloGameViewModel, SoloGameViewModel.SoloGameViewModelFactory> { factory ->
                            factory.create(gameType, seed, position)
                        }
                    SoloGameScreen(
                        viewModel = soloGameViewModel,
                        gameType = gameType,
                        onGameEnd = { navController.navigateUp() }
                    )
                } else {
                    val gameViewModel = hiltViewModel<GameViewModel, GameViewModel.GameViewModelFactory> { factory ->
                        factory.create(gameType, seed, position)
                    }
                    RegularGameScreen(
                        viewModel = gameViewModel,
                        gameType = gameType,
                        navController = navController
                    )
                }
            }
        }
    }
}
