package dev.veryniche.welcometoflip.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MediumTopAppBar
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import dev.veryniche.welcometoflip.ads.InterstitialAdLocation
import dev.veryniche.welcometoflip.components.AboutActionIcon
import dev.veryniche.welcometoflip.components.ChartActionItem
import dev.veryniche.welcometoflip.components.NavigationIcon
import dev.veryniche.welcometoflip.components.ScreenOnToggle
import dev.veryniche.welcometoflip.components.ShopActionIcon
import dev.veryniche.welcometoflip.components.SoloGame
import dev.veryniche.welcometoflip.core.models.GameType
import dev.veryniche.welcometoflip.util.TrackedScreen
import dev.veryniche.welcometoflip.util.getMediumTopAppBarColors
import dev.veryniche.welcometoflip.util.getTopAppBarColors
import dev.veryniche.welcometoflip.util.observeLifecycle
import dev.veryniche.welcometoflip.util.trackScreenView
import dev.veryniche.welcometoflip.viewmodels.SoloGameViewModel
import dev.veryniche.welcometoflip.viewmodels.SoloState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SoloGameScreen(
    snackbarHostState: SnackbarHostState,
    viewModel: SoloGameViewModel,
    gameType: GameType,
    showShopMenuItem: Boolean,
    onShowInterstitialAd: (InterstitialAdLocation) -> Unit,
    onGameEnd: () -> Unit,
    keepScreenOn: Boolean,
    onKeepScreenOnSet: (Boolean) -> Unit,
    keepScreenOnAction: (Boolean) -> Unit,
    navController: NavController = rememberNavController(),
    modifier: Modifier = Modifier
) {
    TrackedScreen {
        trackScreenView(name = gameType.name + " Solo")
    }

    val currentState by viewModel.currentState.collectAsStateWithLifecycle(
        SoloState(gameType = gameType),
    )

//    val position by viewModel.position.observeAsState(viewModel.initialPosition)
//    val advancePositionEnabled by viewModel.advancePositionEnabled.observeAsState(true)
//    val advancePositionEnabled by remember {
//        derivedStateOf {
//            currentState.drawStack.isEmpty() // TODO This is not quite right
//        }
//    }

//    var showEndGameDialog by remember { mutableStateOf(false) }
//    if (isEndGame && showEndGameDialog == false) {
//        showEndGameDialog = true
//    }

//    val phase by viewModel.phase.observeAsState(SoloGamePhase.SETUP)
//    val currentState by viewModel.currentState.observeAsState(SoloState())
//    val activeCardToAstra by viewModel.activeCardToAstra.observeAsState(null)

    viewModel.observeLifecycle(LocalLifecycleOwner.current.lifecycle)

//    val position by viewModel.position.observeAsState(viewModel.initialPosition)
//    val advancePositionEnabled by viewModel.advancePositionEnabled.observeAsState(true)
//    val reversePositionEnabled by viewModel.reversePositionEnabled.observeAsState(true)
//    val isEndGame by viewModel.isEndGame.observeAsState(false)
//    var showEndGameDialog by rememberSaveable(isEndGame) { mutableStateOf(isEndGame) }
//    var showEndGameConfirmationDialog by rememberSaveable { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        keepScreenOnAction.invoke(keepScreenOn)
    }

    DisposableEffect(Unit) {
        onDispose {
            keepScreenOnAction.invoke(false)
        }
    }
    val coroutineScope: CoroutineScope = rememberCoroutineScope()
    val fontScale = LocalDensity.current.fontScale
    Scaffold(
        topBar = {
            if (fontScale > 1.5) {
                MediumTopAppBar(
                    title = {
                        Text(
                            text = stringResource(id = gameType.displayName),
                            maxLines = 2,
                            overflow = TextOverflow.Ellipsis
                        )
                    },
                    navigationIcon = { NavigationIcon(navController = navController) },
                    actions = {
                        ScreenOnToggle(keepScreenOn, onKeepScreenOnSet, snackbarHostState)
                        ChartActionItem(navController, gameType)
                        if (showShopMenuItem) {
                            ShopActionIcon(navController = navController)
                        }
                        AboutActionIcon(navController)
                    },
                    colors = getMediumTopAppBarColors(),
                )
            } else {
                TopAppBar(
                    title = {
                        Text(
                            text = stringResource(id = gameType.displayName),
                            maxLines = 2,
                            overflow = TextOverflow.Ellipsis
                        )
                    },
                    navigationIcon = { NavigationIcon(navController = navController) },
                    actions = {
                        ScreenOnToggle(keepScreenOn, onKeepScreenOnSet, snackbarHostState)
                        ChartActionItem(navController, gameType)
                        if (showShopMenuItem) {
                            ShopActionIcon(navController = navController)
                        }
                        AboutActionIcon(navController)
                    },
                    colors = getTopAppBarColors(),
                )
            }
        },
        snackbarHost = {
            SnackbarHost(hostState = snackbarHostState)
        },
        modifier = modifier
    ) { innerPadding ->
        Box(Modifier.padding(innerPadding)) {
            SoloGame(
                state = currentState,
                onDrawCards = {
                    coroutineScope.launch {
                        viewModel.drawCards()
                    }
                },
                onSelectPlayerCard = { card ->
                    coroutineScope.launch {
                        viewModel.selectPlayerCard(card)
                    }
                },
                onSelectAiCard = {card ->
                    coroutineScope.launch {
                        viewModel.selectAiCard(card)
                    }
                },
                modifier = Modifier
            )
        }

//        GameContainer(
//            displayPosition = 0, // TO FIX //currentState.discardStack + 1,
//            displayEndPosition = 0, // TO FIX  currentState.totalPosition,
// //            gameType = gameType,
//            content = { contentModifier ->
// //            when (phase) {
// //                SoloGamePhase.SETUP -> SoloGameSetup(
// //                    stacks = viewModel.stacks,
// //                    soloPile = viewModel.soloEffectCards,
// //                    onAnimationComplete = {
// //                        viewModel.setupSoloDrawStack()
// //                        viewModel.setupAstraCards()
// //                        viewModel.advancePosition()
// //                    },
// //                    modifier = contentModifier
// //                )
// //                SoloGamePhase.PLAY ->
//                SoloGame(
//                    state = currentState,
//                    onDrawCards = {
//                        coroutineScope.launch {
//                            viewModel.drawCards()
//                        }
//                    },
//                    onSelectAstraCard = {
//                    },
//                    modifier = contentModifier,
//                )
// //            }
//            },
//            modifier = Modifier.padding(
//                top = innerPadding.calculateTopPadding(),
//                start = innerPadding.calculateStartPadding(LayoutDirection.Ltr),
//                end = innerPadding.calculateEndPadding(LayoutDirection.Ltr),
//                bottom = 0.dp
//            )
//        )
    }

//    if (showEndGameDialog) {
//        EndGameDialog(
//            gameType = gameType,
//            position = position,
//            reshuffleStacks = {
//                viewModel.reshuffleStacks()
//            },
//            endGame = {
//                viewModel.endGame(onGameEnd)
//            },
//            onDismissRequest = {
//                showEndGameDialog = false
//            }
//        )
//    }
}
