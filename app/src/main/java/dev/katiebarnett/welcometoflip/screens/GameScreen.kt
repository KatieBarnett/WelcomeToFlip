package dev.katiebarnett.welcometoflip.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
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
             position: Int? = null,
             modifier: Modifier = Modifier
) {
    val position by viewModel.position.observeAsState(position ?: 0)
    val advancePositionEnabled by viewModel.advancePositionEnabled.observeAsState(true)
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
        reshuffle = { 
            viewModel.reshuffle()
        },
        modifier = modifier
    )
}

@Composable
fun Game(position: Int,
         gameType: GameType,
         stacks: List<List<Card>>,
         advancePosition: () -> Unit,
         advancePositionEnabled: Boolean,
         reshuffle: () -> Unit,
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
            Text(stringResource(id = R.string.deck_position, position, stacks.getStackSize() ?: 0))
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
            ThemedButton(onClick = {
                reshuffle.invoke()
            }, modifier = modifier.weight(1f)) {
                Text(stringResource(id = R.string.reshuffle_button))
            }
        }
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
        Game(0, WelcomeToTheMoon, stacks, {}, true, {}, Modifier)
    }
}