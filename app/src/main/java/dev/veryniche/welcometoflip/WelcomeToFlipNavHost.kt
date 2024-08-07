package dev.veryniche.welcometoflip

import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.window.DialogProperties
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.dialog
import dev.veryniche.welcometoflip.ads.InterstitialAdLocation
import dev.veryniche.welcometoflip.core.models.mapToGameType
import dev.veryniche.welcometoflip.purchase.InAppProduct
import dev.veryniche.welcometoflip.screens.AboutScreen
import dev.veryniche.welcometoflip.screens.ChartScreen
import dev.veryniche.welcometoflip.screens.ChooseGameScreen
import dev.veryniche.welcometoflip.screens.RegularGameScreen
import dev.veryniche.welcometoflip.screens.ShopScreen
import dev.veryniche.welcometoflip.screens.SoloGameScreen
import dev.veryniche.welcometoflip.util.SetDialogDestinationToEdgeToEdge
import dev.veryniche.welcometoflip.util.isAvailablePurchases
import dev.veryniche.welcometoflip.viewmodels.ChartViewModel
import dev.veryniche.welcometoflip.viewmodels.GameViewModel
import dev.veryniche.welcometoflip.viewmodels.MainViewModel
import dev.veryniche.welcometoflip.viewmodels.SoloGameViewModel

@Composable
fun WelcomeToFlipNavHost(
    navController: NavHostController,
    snackbarHostState: SnackbarHostState,
    onGameEnd: () -> Unit,
    keepScreenOn: Boolean,
    onKeepScreenOnSet: (Boolean) -> Unit,
    keepScreenOnAction: (Boolean) -> Unit,
    showPurchaseErrorMessage: (Int) -> Unit,
    onShowInterstitialAd: (InterstitialAdLocation) -> Unit,
    modifier: Modifier = Modifier,
    mainViewModel: MainViewModel,
    purchaseStatus: Map<String, InAppProduct>,
    windowSizeClass: WindowSizeClass,
) {
    NavHost(
        navController = navController,
        startDestination = ChooseGame.route,
        modifier = modifier
    ) {
        composable(route = ChooseGame.route) {
            ChooseGameScreen(
                navController = navController,
                snackbarHostState = snackbarHostState,
                viewModel = mainViewModel,
                windowSizeClass = windowSizeClass,
                onShowInterstitialAd = onShowInterstitialAd,
                showShopMenuItem = purchaseStatus.isAvailablePurchases(),
                onPurchaseError = {
                    showPurchaseErrorMessage.invoke(it)
                }
            )
        }
        composable(route = About.route) {
            AboutScreen(
                navController = navController,
                snackbarHostState = snackbarHostState,
                purchaseStatus = purchaseStatus,
                windowSizeClass = windowSizeClass,
                showShopMenuItem = purchaseStatus.isAvailablePurchases(),
                onPurchaseClick = { productId ->
                    mainViewModel.purchaseProduct(productId) {
                        showPurchaseErrorMessage.invoke(it)
                    }
                },
            )
        }
        composable(route = Shop.route) {
            ShopScreen(
                navController = navController,
                snackbarHostState = snackbarHostState,
                purchaseStatus = purchaseStatus,
                windowSizeClass = windowSizeClass,
                onPurchaseClick = { productId ->
                    mainViewModel.purchaseProduct(productId) {
                        showPurchaseErrorMessage.invoke(it)
                    }
                },
            )
        }
        dialog(
            route = ChartScreen.routeWithArgs,
            arguments = ChartScreen.arguments,
            deepLinks = ChartScreen.deepLinks,
            dialogProperties = DialogProperties(
                usePlatformDefaultWidth = true,
                decorFitsSystemWindows = false
            )
        ) {
            it.arguments?.getString(ChartScreen.gameTypeArg)?.mapToGameType()?.let { gameType ->
                val chartViewModel = hiltViewModel<ChartViewModel, ChartViewModel.ChartViewModelFactory> { factory ->
                    factory.create(gameType)
                }
                SetDialogDestinationToEdgeToEdge()
                ChartScreen(
                    navController = navController,
                    snackbarHostState = snackbarHostState,
                    gameType = gameType,
                    deck = chartViewModel.deck,
                    modifier = Modifier
                )
            }
        }
        composable(route = Game.routeWithArgs, arguments = Game.arguments, deepLinks = Game.deepLinks) {
            it.arguments?.getString(Game.gameTypeArg)?.mapToGameType()?.let { gameType ->
                val seed = it.arguments?.getString(Game.seedArg)?.toLong() ?: System.currentTimeMillis()
                val position = it.arguments?.getString(Game.positionArg)?.toInt() ?: 0
                val gameViewModel = hiltViewModel<GameViewModel, GameViewModel.GameViewModelFactory> { factory ->
                    factory.create(gameType, seed, position)
                }
                RegularGameScreen(
                    viewModel = gameViewModel,
                    snackbarHostState = snackbarHostState,
                    gameType = gameType,
                    onGameEnd = onGameEnd,
                    navController = navController,
                    onShowInterstitialAd = onShowInterstitialAd,
                    showShopMenuItem = purchaseStatus.isAvailablePurchases(),
                    keepScreenOn = keepScreenOn,
                    onKeepScreenOnSet = onKeepScreenOnSet,
                    keepScreenOnAction = keepScreenOnAction
                )
            }
        }
        composable(route = SoloGame.routeWithArgs, arguments = SoloGame.arguments, deepLinks = SoloGame.deepLinks) {
            it.arguments?.getString(SoloGame.gameTypeArg)?.mapToGameType()?.let { gameType ->
                val seed = it.arguments?.getString(Game.seedArg)?.toLong() ?: System.currentTimeMillis()
                val position = it.arguments?.getString(Game.positionArg)?.toInt() ?: 0

                val soloGameViewModel =
                    hiltViewModel<SoloGameViewModel, SoloGameViewModel.SoloGameViewModelFactory> { factory ->
                        factory.create(gameType, seed, position)
                    }
                SoloGameScreen(
                    viewModel = soloGameViewModel,
                    snackbarHostState = snackbarHostState,
                    gameType = gameType,
                    onGameEnd = onGameEnd,
                    navController = navController,
                    onShowInterstitialAd = onShowInterstitialAd,
                    showShopMenuItem = purchaseStatus.isAvailablePurchases(),
                    keepScreenOn = keepScreenOn,
                    onKeepScreenOnSet = onKeepScreenOnSet,
                    keepScreenOnAction = keepScreenOnAction
                )
            }
        }
    }
}
