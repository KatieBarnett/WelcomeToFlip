package dev.veryniche.welcometoflip

import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import dev.veryniche.welcometoflip.ads.InterstitialAdLocation
import dev.veryniche.welcometoflip.core.models.mapToGameType
import dev.veryniche.welcometoflip.purchase.InAppProduct
import dev.veryniche.welcometoflip.screens.AboutScreen
import dev.veryniche.welcometoflip.screens.ChooseGameScreen
import dev.veryniche.welcometoflip.screens.RegularGameScreen
import dev.veryniche.welcometoflip.screens.ShopScreen
import dev.veryniche.welcometoflip.screens.SoloGameScreen
import dev.veryniche.welcometoflip.util.isAvailablePurchases

@Composable
fun WelcomeToFlipNavHost(
    navController: NavHostController,
    onGameEnd: () -> Unit,
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
                purchaseStatus = purchaseStatus,
                windowSizeClass = windowSizeClass,
                onPurchaseClick = { productId ->
                    mainViewModel.purchaseProduct(productId) {
                        showPurchaseErrorMessage.invoke(it)
                    }
                },
            )
        }
        composable(route = Game.routeWithArgs, arguments = Game.arguments, deepLinks = Game.deepLinks) {
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
                        navController = navController,
                        windowSizeClass = windowSizeClass,
                        onShowInterstitialAd = onShowInterstitialAd,
                        showShopMenuItem = purchaseStatus.isAvailablePurchases(),
                    )
                }
            }
        }
    }
}
