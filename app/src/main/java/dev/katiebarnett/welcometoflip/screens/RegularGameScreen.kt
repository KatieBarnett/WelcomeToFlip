package dev.katiebarnett.welcometoflip.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.tooling.preview.Preview
import dev.katiebarnett.welcometoflip.GameViewModel
import dev.katiebarnett.welcometoflip.components.EndGameDialog
import dev.katiebarnett.welcometoflip.components.GameContainer
import dev.katiebarnett.welcometoflip.components.Stack
import dev.katiebarnett.welcometoflip.core.models.*
import dev.katiebarnett.welcometoflip.theme.Dimen
import dev.katiebarnett.welcometoflip.theme.WelcomeToFlipTheme
import dev.katiebarnett.welcometoflip.util.getStackSize
import dev.katiebarnett.welcometoflip.util.observeLifecycle

@Composable
fun RegularGameBody(viewModel: GameViewModel,
                    gameType: GameType,
                    seed: Long? = null,
                    initialPosition: Int? = null,
                    onGameEnd: () -> Unit,
                    modifier: Modifier = Modifier
) {
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
        )},
        modifier = modifier
    )
    
    if (showEndGameDialog) {
        EndGameDialog(
            reshuffleStacks = {
                viewModel.reshuffleStacks()
            },
            endGame = {
                viewModel.endGame(onGameEnd)
            },
            onDismissRequest = {
                showEndGameDialog = false
            }
        )
    }
}

@Composable
fun RegularGame(position: Int,
         stacks: List<List<Card>>,
         modifier: Modifier = Modifier) {
    Column(
        verticalArrangement = Arrangement.spacedBy(Dimen.spacing),
        modifier = modifier) {
        stacks.forEach { stack ->
            Stack(stack, position, Modifier.weight(1f))
        }        
    }
}

@Preview(showBackground = true)
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