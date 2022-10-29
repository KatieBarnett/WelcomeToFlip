package dev.katiebarnett.welcometoflip.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import dev.katiebarnett.welcometoflip.R
import dev.katiebarnett.welcometoflip.core.models.GameType
import dev.katiebarnett.welcometoflip.core.models.WelcomeToTheMoon
import dev.katiebarnett.welcometoflip.theme.Dimen
import dev.katiebarnett.welcometoflip.theme.WelcomeToFlipTheme
import dev.katiebarnett.welcometoflip.util.trackEndGame
import dev.katiebarnett.welcometoflip.util.trackShuffleGame


@Composable
fun GameContainer(displayPosition: Int, 
                  displayEndPosition: Int,
                  gameType: GameType,
                  advancePosition: () -> Unit,
                  advancePositionEnabled: Boolean,
                  content: @Composable (modifier: Modifier) -> Unit,
                  modifier: Modifier = Modifier) {
    Column(
        verticalArrangement = Arrangement.spacedBy(Dimen.spacing),
        modifier = modifier
            .fillMaxSize()
            .padding(Dimen.spacing)
    ) {
        Row(modifier = Modifier
            .fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(Dimen.spacing)) {
            Text(stringResource(id = gameType.displayName), modifier = Modifier.weight(1f))
            Text(stringResource(id = R.string.deck_position, displayPosition, displayEndPosition))
        }
        content(modifier = Modifier
            .weight(1f))
        Row( modifier = Modifier
            .fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(Dimen.Button.spacing)) {
            ThemedButton(onClick = {
                advancePosition.invoke()
            }, enabled = advancePositionEnabled, modifier = Modifier.weight(1f)) {
                Text(stringResource(id = R.string.flip_button))
            }
        }
    }
}

@Composable
fun SoloGameContainer(displayPosition: Int,
                  displayEndPosition: Int,
                  gameType: GameType,
                  content: @Composable (modifier: Modifier) -> Unit,
                  modifier: Modifier = Modifier) {
    Column(
        verticalArrangement = Arrangement.spacedBy(Dimen.spacing),
        modifier = modifier
            .fillMaxSize()
            .padding(Dimen.spacing)
    ) {
        Row(modifier = Modifier
            .fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(Dimen.spacing)) {
            Text(stringResource(id = gameType.displayName), modifier = modifier.weight(1f))
            Text(stringResource(id = R.string.deck_position, displayPosition, displayEndPosition))
        }
        content(modifier = Modifier
            .weight(1f))
    }
}

@Composable
fun EndGameDialog(gameType: GameType,
                  position: Int,
                  reshuffleStacks: () -> Unit,
                  endGame: () -> Unit,
                  onDismissRequest: () -> Unit
) {
    AnimatedTransitionDialog(onDismissRequest = onDismissRequest) { dialogHelper ->
        Surface(
            shape = RoundedCornerShape(Dimen.Dialog.radius),
            color = MaterialTheme.colorScheme.surface,
            modifier = Modifier
        ) {
            Column(
                verticalArrangement = Arrangement.spacedBy(Dimen.spacing),
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.padding(Dimen.spacing)
            ) {
                Text(stringResource(id = R.string.stack_end_message))
                ThemedButton(onClick = {
                    trackShuffleGame(gameType, position)
                    reshuffleStacks.invoke()
                    dialogHelper::triggerAnimatedDismiss.invoke()
                }) {
                    Text(stringResource(id = R.string.reshuffle_button))
                }
                ThemedButton(onClick = {
                    trackEndGame(gameType, position)
                    dialogHelper::triggerAnimatedDismiss.invoke()
                    endGame.invoke() }) {
                    Text(stringResource(id = R.string.end_game_button))
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun GameContainerPreview() {
    WelcomeToFlipTheme {
        GameContainer(
            displayPosition = 1,
            displayEndPosition = 10, 
            gameType = WelcomeToTheMoon,
            advancePosition = {},
            advancePositionEnabled = true,
            content = { modifier ->
                Text("Game content", modifier = modifier)
            },
            modifier = Modifier
        )
    }
}

@Preview(showBackground = true)
@Composable
fun EndGameDialogPreview() {
    WelcomeToFlipTheme {
        EndGameDialog(WelcomeToTheMoon, 8, reshuffleStacks = {}, endGame = {}, onDismissRequest = {})
    }
}