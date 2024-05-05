package dev.veryniche.welcometoflip.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import dev.veryniche.welcometoflip.GameViewModel
import dev.veryniche.welcometoflip.components.AboutActionIcon
import dev.veryniche.welcometoflip.components.EndGameConfirmationDialog
import dev.veryniche.welcometoflip.components.EndGameDialog
import dev.veryniche.welcometoflip.components.GameContainer
import dev.veryniche.welcometoflip.components.NavigationIcon
import dev.veryniche.welcometoflip.components.Stack
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
import dev.veryniche.welcometoflip.util.getStackSize
import dev.veryniche.welcometoflip.util.observeLifecycle
import dev.veryniche.welcometoflip.util.trackScreenView

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegularGameScreen(
    viewModel: GameViewModel,
    gameType: GameType,
    onGameEnd: () -> Unit,
    navController: NavController = rememberNavController(),
    modifier: Modifier = Modifier
) {
    TrackedScreen {
        trackScreenView(name = gameType.name)
    }

    val position by viewModel.position.observeAsState(viewModel.initialPosition)
    val advancePositionEnabled by viewModel.advancePositionEnabled.observeAsState(true)
    val reversePositionEnabled by viewModel.reversePositionEnabled.observeAsState(true)
    val isEndGame by viewModel.isEndGame.observeAsState(false)
    var showEndGameDialog by remember(isEndGame) { mutableStateOf(isEndGame) }
    var showEndGameConfirmationDialog by remember { mutableStateOf(false) }

    viewModel.observeLifecycle(LocalLifecycleOwner.current.lifecycle)

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
            advancePosition = {
                viewModel.advancePosition()
            },
            advancePositionEnabled = advancePositionEnabled,
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
