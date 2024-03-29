package dev.katiebarnett.welcometoflip.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import dev.katiebarnett.welcometoflip.GameViewModel
import dev.katiebarnett.welcometoflip.components.AboutActionIcon
import dev.katiebarnett.welcometoflip.components.EndGameDialog
import dev.katiebarnett.welcometoflip.components.GameContainer
import dev.katiebarnett.welcometoflip.components.NavigationIcon
import dev.katiebarnett.welcometoflip.components.Stack
import dev.katiebarnett.welcometoflip.core.models.*
import dev.katiebarnett.welcometoflip.theme.Dimen
import dev.katiebarnett.welcometoflip.theme.WelcomeToFlipTheme
import dev.katiebarnett.welcometoflip.util.TrackedScreen
import dev.katiebarnett.welcometoflip.util.getStackSize
import dev.katiebarnett.welcometoflip.util.observeLifecycle
import dev.katiebarnett.welcometoflip.util.trackScreenView

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegularGameScreen(
    viewModel: GameViewModel,
    gameType: GameType,
    seed: Long? = null,
    initialPosition: Int? = null,
    navController: NavController = rememberNavController(),
    modifier: Modifier = Modifier
) {
    TrackedScreen {
        trackScreenView(name = gameType.name)
    }

    val position by viewModel.position.observeAsState(initialPosition ?: viewModel.initialPosition)
    val advancePositionEnabled by viewModel.advancePositionEnabled.observeAsState(true)
    val isEndGame by viewModel.isEndGame.observeAsState(false)
    val gameSeed by rememberSaveable { mutableStateOf(seed ?: System.currentTimeMillis()) }

    var showEndGameDialog by remember { mutableStateOf(false) }
    if (isEndGame && showEndGameDialog == false) {
        showEndGameDialog = true
    }

    viewModel.observeLifecycle(LocalLifecycleOwner.current.lifecycle)
    viewModel.initialiseGame(gameType, gameSeed, position)

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = stringResource(id = gameType.displayName)) },
                navigationIcon = { NavigationIcon(navController = navController) },
                actions = { AboutActionIcon(navController) }
            )
        },
        modifier = modifier
    ) { innerPadding ->
        GameContainer(
            displayPosition = position + 1,
            displayEndPosition = viewModel.stacks.getStackSize() ?: 0,
            gameType = gameType,
            advancePosition = {
                viewModel.advancePosition()
            },
            advancePositionEnabled = advancePositionEnabled,
            content = { contentModifier ->
                RegularGame(
                    position = position,
                    stacks = viewModel.stacks,
                    modifier = contentModifier
                )
            },
            modifier = Modifier.padding(innerPadding)
        )
    }

    if (showEndGameDialog) {
        EndGameDialog(
            gameType = gameType,
            position = position,
            reshuffleStacks = {
                viewModel.reshuffleStacks()
            },
            endGame = {
                viewModel.endGame {
                    navController.navigateUp()
                }
            },
            onDismissRequest = {
                showEndGameDialog = false
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

@Preview(group = "Regular Game Screen", showBackground = true)
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
