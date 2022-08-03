package dev.katiebarnett.welcometoflip.screens

import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.window.Dialog
import dev.katiebarnett.welcometoflip.GameViewModel
import dev.katiebarnett.welcometoflip.R
import dev.katiebarnett.welcometoflip.components.Stack
import dev.katiebarnett.welcometoflip.components.ThemedButton
import dev.katiebarnett.welcometoflip.core.models.*
import dev.katiebarnett.welcometoflip.theme.Dimen
import dev.katiebarnett.welcometoflip.theme.WelcomeToFlipTheme
import dev.katiebarnett.welcometoflip.util.getStackSize
import dev.katiebarnett.welcometoflip.util.observeLifecycle

@Composable
fun GameBody(viewModel: GameViewModel,
             gameType: GameType,
             seed: Long? = null, 
             initialPosition: Int? = null,
             onGameEnd: () -> Unit, 
             modifier: Modifier = Modifier
) {
    val position by viewModel.position.observeAsState(initialPosition ?: 0)
    val advancePositionEnabled by viewModel.advancePositionEnabled.observeAsState(true)
    val showEndGame by viewModel.showEndGame.observeAsState(false)
    val gameSeed by rememberSaveable { mutableStateOf(seed ?: System.currentTimeMillis()) }

    viewModel.observeLifecycle(LocalLifecycleOwner.current.lifecycle)
    viewModel.initialiseGame(gameType, gameSeed, position)
    
    Game(
        position = position,
        gameType = gameType,
        stacks = viewModel.stacks,
        advancePosition = {
            viewModel.advancePosition()
        },
        advancePositionEnabled = advancePositionEnabled,
        modifier = modifier
    )
    
    if (showEndGame) {
        Log.d("AdvancePosition", "showing end game")
        EndGameDialog(
            reshuffleStacks = { 
                viewModel.reshuffleStacks() 
            },
            endGame = {
                viewModel.endGame(onGameEnd)
            }
        )
    }
}

@Composable
fun Game(position: Int,
         gameType: GameType,
         stacks: List<List<Card>>,
         advancePosition: () -> Unit,
         advancePositionEnabled: Boolean,
         modifier: Modifier = Modifier) {
    Column(
        modifier
            .fillMaxSize()
            .padding(Dimen.spacing)
    ) {
        Row(modifier = modifier
            .padding(Dimen.spacing)
            .fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(Dimen.spacing)) {
            Text(stringResource(id = gameType.displayName), modifier = modifier.weight(1f))
            Text(stringResource(id = R.string.deck_position, position + 1, stacks.getStackSize() ?: 0))
        }
        stacks.forEach { stack ->
            Stack(stack, position, modifier.weight(1f))
        }
        Row( modifier = modifier
            .padding(Dimen.spacing)
            .fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(Dimen.Button.spacing)) {
            ThemedButton(onClick = {
                advancePosition.invoke()
            }, enabled = advancePositionEnabled, modifier = modifier.weight(1f)) {
                Text(stringResource(id = R.string.flip_button))
            }
        }
    }
}

@Composable
fun EndGameDialog(reshuffleStacks: () -> Unit, 
                  endGame: () -> Unit, 
                  modifier: Modifier = Modifier) {
    Dialog(onDismissRequest = {  }) {
        Surface(
            shape = RoundedCornerShape(Dimen.Dialog.radius),
            color = MaterialTheme.colorScheme.surface, 
            modifier = modifier
        ) {
            Column(
                verticalArrangement = Arrangement.spacedBy(Dimen.spacing),
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.padding(Dimen.spacing)
            ) {
                Text(stringResource(id = R.string.stack_end_message))
                ThemedButton(onClick = { reshuffleStacks.invoke() }) {
                    Text(stringResource(id = R.string.reshuffle_button))
                }
                ThemedButton(onClick = { endGame.invoke() }) {
                    Text(stringResource(id = R.string.end_game_button))
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun EndGameDialogPreview() {
    WelcomeToFlipTheme {
        EndGameDialog(reshuffleStacks = {}, endGame = {})
    }
}

@Preview(showBackground = true)
@Composable
fun GamePreview() {

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
        Game(0, WelcomeToTheMoon, stacks, {}, true, Modifier)
    }
}