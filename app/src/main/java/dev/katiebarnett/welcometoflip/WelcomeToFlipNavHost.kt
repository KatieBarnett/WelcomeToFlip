package dev.katiebarnett.welcometoflip

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import dev.katiebarnett.welcometoflip.data.mapToGameType
import dev.katiebarnett.welcometoflip.screens.ChooseGameBody
import dev.katiebarnett.welcometoflip.screens.GameBody

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
            ChooseGameBody(chooseGameAction = {
                navController.navigate(route = Game.getRoute(it))
            })
        }
        composable(route = Game.routeWithArgs, arguments = Game.arguments, deepLinks = Game.deepLinks) {
            it.arguments?.getString(Game.gameTypeArg)?.mapToGameType()?.let { gameType ->
                val viewModel = hiltViewModel<GameViewModel>()
                viewModel.initialiseGame(gameType)
                GameBody(
                    viewModel = viewModel,
                    gameType = gameType
                )
            }
        }
    }
}