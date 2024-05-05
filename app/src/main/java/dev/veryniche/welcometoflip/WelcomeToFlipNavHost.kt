package dev.veryniche.welcometoflip

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
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
    onGameEnd: () -> Unit,
    modifier: Modifier = Modifier,
    mainViewModel: MainViewModel,
) {
    var showPurchaseErrorMessage by rememberSaveable { mutableStateOf<Int?>(null) }
    val aboutPurchaseStatus by mainViewModel.aboutPurchaseStatus.collectAsStateWithLifecycle(mapOf())
    NavHost(
        navController = navController,
        startDestination = ChooseGame.route,
        modifier = modifier
    ) {
        composable(route = ChooseGame.route) {
            ChooseGameScreen(
                navController = navController,
                viewModel = mainViewModel,
                onPurchaseError = {
                    showPurchaseErrorMessage = it
                }
            )
        }
        composable(route = About.route) {
            AboutScreen(
                navController = navController,
                purchaseStatus = aboutPurchaseStatus,
                onPurchaseClick = { productId ->
                    mainViewModel.purchaseProduct(productId) {
                        showPurchaseErrorMessage = it
                    }
                },
            )
        }
        composable(route = Game.routeWithArgs, arguments = Game.arguments, deepLinks = Game.deepLinks) {
            // TODO handle navigation errors
            it.arguments?.getString(Game.gameTypeArg)?.mapToGameType()?.let { gameType ->
                val seed = it.arguments?.getString(Game.seedArg)?.toLong() ?: System.currentTimeMillis()
                val position = it.arguments?.getString(Game.positionArg)?.toInt() ?: 0

                if (gameType.soloAvailable) {
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
                        onGameEnd = onGameEnd,
                        navController = navController
                    )
                }
            }
        }
    }

    showPurchaseErrorMessage?.let { message ->
        AlertDialog(
            onDismissRequest = { showPurchaseErrorMessage = null },
            title = { Text(stringResource(R.string.app_name)) },
            text = { Text(stringResource(message)) },
            confirmButton = {
                TextButton(onClick = {
                    showPurchaseErrorMessage = null
                }) {
                    Text(stringResource(R.string.purchase_error_dismiss))
                }
            }
        )
    }
}
