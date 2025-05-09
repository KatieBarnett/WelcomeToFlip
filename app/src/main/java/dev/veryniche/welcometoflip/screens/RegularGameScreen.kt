package dev.veryniche.welcometoflip.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MediumTopAppBar
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import dev.veryniche.welcometoflip.R
import dev.veryniche.welcometoflip.viewmodels.GameViewModel
import dev.veryniche.welcometoflip.ads.InterstitialAdLocation
import dev.veryniche.welcometoflip.components.AboutActionIcon
import dev.veryniche.welcometoflip.components.ChartActionItem
import dev.veryniche.welcometoflip.components.EndGameConfirmationDialog
import dev.veryniche.welcometoflip.components.EndGameDialog
import dev.veryniche.welcometoflip.components.GameContainer
import dev.veryniche.welcometoflip.components.NavigationIcon
import dev.veryniche.welcometoflip.components.ScreenOnToggle
import dev.veryniche.welcometoflip.components.ShopActionIcon
import dev.veryniche.welcometoflip.components.Stack
import dev.veryniche.welcometoflip.components.ThemedButton
import dev.veryniche.welcometoflip.core.models.Astronaut
import dev.veryniche.welcometoflip.core.models.Card
import dev.veryniche.welcometoflip.core.models.GameType
import dev.veryniche.welcometoflip.core.models.Lightning
import dev.veryniche.welcometoflip.core.models.Number1
import dev.veryniche.welcometoflip.core.models.Number2
import dev.veryniche.welcometoflip.core.models.Number3
import dev.veryniche.welcometoflip.core.models.Plant
import dev.veryniche.welcometoflip.core.models.Robot
import dev.veryniche.welcometoflip.core.models.Water
import dev.veryniche.welcometoflip.core.models.X
import dev.veryniche.welcometoflip.theme.Dimen
import dev.veryniche.welcometoflip.theme.WelcomeToFlipTheme
import dev.veryniche.welcometoflip.util.TrackedScreen
import dev.veryniche.welcometoflip.util.getMediumTopAppBarColors
import dev.veryniche.welcometoflip.util.getStackSize
import dev.veryniche.welcometoflip.util.getTopAppBarColors
import dev.veryniche.welcometoflip.util.observeLifecycle
import dev.veryniche.welcometoflip.util.trackScreenView

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegularGameScreen(
    snackbarHostState: SnackbarHostState,
    viewModel: GameViewModel,
    gameType: GameType,
    showShopMenuItem: Boolean,
    onShowInterstitialAd: (InterstitialAdLocation) -> Unit,
    onGameEnd: () -> Unit,
    keepScreenOn: Boolean,
    onKeepScreenOnSet: (Boolean) -> Unit,
    keepScreenOnAction: (Boolean) -> Unit,
    navController: NavController = rememberNavController(),
    modifier: Modifier = Modifier,
) {
    TrackedScreen {
        trackScreenView(name = gameType.name)
    }

    val position by viewModel.position.observeAsState(viewModel.initialPosition)
    val advancePositionEnabled by viewModel.advancePositionEnabled.observeAsState(true)
    val reversePositionEnabled by viewModel.reversePositionEnabled.observeAsState(true)
    val isEndGame by viewModel.isEndGame.observeAsState(false)
    var showEndGameDialog by rememberSaveable(isEndGame) { mutableStateOf(isEndGame) }
    var showEndGameConfirmationDialog by rememberSaveable { mutableStateOf(false) }

    viewModel.observeLifecycle(LocalLifecycleOwner.current.lifecycle)

    LaunchedEffect(Unit) {
        keepScreenOnAction.invoke(keepScreenOn)
    }

    DisposableEffect(Unit) {
        onDispose {
            keepScreenOnAction.invoke(false)
        }
    }
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
        GameContainer(
            displayPosition = position + 1,
            displayEndPosition = viewModel.stacks.getStackSize() ?: 0,
            button  = { modifier ->
                ThemedButton(onClick = {
                    viewModel.advancePosition()
                }, enabled = advancePositionEnabled, modifier = modifier) {
                    Text(
                        text = stringResource(id = R.string.flip_button),
                        style = MaterialTheme.typography.displayMedium
                    )
                }
            },
            reversePosition = {
                viewModel.reversePosition()
            },
            reversePositionEnabled = reversePositionEnabled,
            content = { contentModifier ->
                RegularGame(
                    position = position,
                    stacks = viewModel.stacks,
                    modifier = contentModifier
                )
            },
            endGame = {
                showEndGameConfirmationDialog = true
            },
            modifier = Modifier.padding(
                top = innerPadding.calculateTopPadding(),
                start = innerPadding.calculateStartPadding(LayoutDirection.Ltr),
                end = innerPadding.calculateEndPadding(LayoutDirection.Ltr),
                bottom = 0.dp
            )
        )
    }

    if (showEndGameDialog) {
        EndGameDialog(
            gameType = gameType,
            position = position,
            reshuffleStacks = {
                onShowInterstitialAd.invoke(InterstitialAdLocation.ReshuffleStacks)
                viewModel.reshuffleStacks()
            },
            endGame = {
                onShowInterstitialAd.invoke(InterstitialAdLocation.EndGame)
                onGameEnd.invoke()
                viewModel.endGame {
                    navController.navigateUp()
                }
            },
            onDismissRequest = {
                showEndGameDialog = false
            }
        )
    }

    if (showEndGameConfirmationDialog) {
        EndGameConfirmationDialog(
            gameType = gameType,
            position = position,
            endGame = {
                onShowInterstitialAd.invoke(InterstitialAdLocation.EndGame)
                onGameEnd.invoke()
                viewModel.endGame {
                    navController.navigateUp()
                }
            },
            onDismissRequest = {
                showEndGameConfirmationDialog = false
            }
        )
    }
}

@Composable
fun RegularGame(
    position: Int,
    stacks: List<List<Card>>,
    modifier: Modifier = Modifier
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(Dimen.spacing),
        modifier = modifier
    ) {
        stacks.forEach { stack ->
            Stack(stack, position, Modifier.weight(1f))
        }
    }
}

@Preview(showBackground = true)
@Preview(showBackground = true, device = Devices.TABLET)
@Preview(showBackground = true, device = "spec:id=reference_tablet,shape=Normal,width=800,height=1280,unit=dp,dpi=240")
@Composable
fun RegularGamePreview() {
    val stacks = listOf(
        listOf(
            Card(Robot, Number1),
            Card(Lightning, Number1)
        ),
        listOf(
            Card(Lightning, Number2),
            Card(Plant, Number2)
        ),
        listOf(
            Card(X, Number3),
            Card(Astronaut, Number3),
            Card(Water, Number3)
        )
    )

    WelcomeToFlipTheme {
        RegularGame(1, stacks, Modifier)
    }
}
