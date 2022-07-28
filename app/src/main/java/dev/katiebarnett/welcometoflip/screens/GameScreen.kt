@file:OptIn(ExperimentalMaterial3Api::class)

package dev.katiebarnett.welcometoflip.screens

import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.google.android.material.composethemeadapter.MdcTheme
import dev.katiebarnett.welcometoflip.GameViewModel
import dev.katiebarnett.welcometoflip.R
import dev.katiebarnett.welcometoflip.components.Stack
import dev.katiebarnett.welcometoflip.data.*
import dev.katiebarnett.welcometoflip.models.Card
import dev.katiebarnett.welcometoflip.util.getStackSize

@Composable
fun GameBody(viewModel: GameViewModel,
             gameType: GameType,
             modifier: Modifier = Modifier
) {
    val position by viewModel.position.observeAsState(0)
    val advancePositionEnabled by viewModel.advancePositionEnabled.observeAsState(true)
    val gameSeed by rememberSaveable { mutableStateOf(System.currentTimeMillis()) }
    
    viewModel.initialiseGame(gameType, gameSeed)
    
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
            .padding(dimensionResource(id = R.dimen.spacing))
    ) {
        Row(modifier = modifier
            .padding(dimensionResource(id = R.dimen.spacing))
            .fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(dimensionResource(id = R.dimen.spacing))) {
            Text(stringResource(id = gameType.displayName), modifier = modifier.weight(1f))
            Text(stringResource(id = R.string.deck_position, position, stacks.getStackSize() ?: 0))
        }
        stacks.forEach { stack ->
            Stack(stack, position, modifier.weight(1f))
        }
        Row( modifier = modifier
            .padding(dimensionResource(id = R.dimen.spacing))
            .fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(dimensionResource(id = R.dimen.button_spacing))) {
            Button(onClick = {
                advancePosition.invoke()
            }, enabled = advancePositionEnabled, modifier = modifier.weight(1f)) {
                Text(stringResource(id = R.string.flip_button))
            }
            Button(onClick = {
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
    
    MdcTheme {
        Game(0, WelcomeToTheMoon, stacks, {}, true, {}, Modifier)
    }
}