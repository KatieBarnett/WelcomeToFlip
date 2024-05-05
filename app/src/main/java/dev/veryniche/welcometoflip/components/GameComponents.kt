package dev.veryniche.welcometoflip.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import dev.veryniche.welcometoflip.R
import dev.veryniche.welcometoflip.core.models.GameType
import dev.veryniche.welcometoflip.core.models.WelcomeToTheMoon
import dev.veryniche.welcometoflip.theme.Dimen
import dev.veryniche.welcometoflip.theme.WelcomeToFlipTheme
import dev.veryniche.welcometoflip.util.trackEndGame
import dev.veryniche.welcometoflip.util.trackShuffleGame

@Composable
fun GameContainer(
    displayPosition: Int,
    displayEndPosition: Int,
    advancePosition: () -> Unit,
    advancePositionEnabled: Boolean,
    reversePosition: () -> Unit,
    reversePositionEnabled: Boolean,
    endGame: () -> Unit,
    content: @Composable (Modifier) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(Dimen.spacing),
        modifier = modifier
            .fillMaxSize()
            .padding(Dimen.spacing)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(Dimen.spacing)
        ) {
            Text(
                text = stringResource(id = R.string.deck_position, displayPosition, displayEndPosition),
                modifier = Modifier.weight(1f)
            )
            ThemedButtonWithIcon(
                iconRes = dev.veryniche.welcometoflip.core.R.drawable.noun_undo_4100779,
                textRes = null,
                imageAltTextRes = R.string.flip_back_button,
                onClick = {
                    reversePosition.invoke()
                },
                enabled = reversePositionEnabled,
                modifier = Modifier
            )
            ThemedButton(onClick = {
                endGame.invoke()
            }, modifier = Modifier) {
                Text(stringResource(id = R.string.end_game_button))
            }
        }
        content(Modifier.weight(1f))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(Dimen.Button.spacing)
        ) {
            ThemedButton(onClick = {
                advancePosition.invoke()
            }, enabled = advancePositionEnabled, modifier = Modifier.weight(1f)) {
                Text(
                    text = stringResource(id = R.string.flip_button),
                    style = MaterialTheme.typography.displayMedium
                )
            }
        }
    }
}

@Composable
fun SoloGameContainer(
    displayPosition: Int,
    displayEndPosition: Int,
    gameType: GameType,
    content: @Composable (modifier: Modifier) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(Dimen.spacing),
        modifier = modifier
            .fillMaxSize()
            .padding(Dimen.spacing)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(Dimen.spacing)
        ) {
            Text(stringResource(id = gameType.displayName), modifier = modifier.weight(1f))
            Text(stringResource(id = R.string.deck_position, displayPosition, displayEndPosition))
        }
        content(
            modifier = Modifier
                .weight(1f)
        )
    }
}

@Composable
fun EndGameDialog(
    gameType: GameType,
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
                modifier = Modifier.padding(Dimen.spacingDouble)
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
                    endGame.invoke()
                }) {
                    Text(stringResource(id = R.string.end_game_button))
                }
            }
        }
    }
}

@Composable
fun EndGameConfirmationDialog(
    gameType: GameType,
    position: Int,
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
                modifier = Modifier.padding(Dimen.spacingDouble)
            ) {
                Text(stringResource(id = R.string.game_end_confirmation_message))
                Row(
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    ThemedButton(onClick = {
                        trackEndGame(gameType, position)
                        dialogHelper::triggerAnimatedDismiss.invoke()
                        endGame.invoke()
                    }) {
                        Text(stringResource(id = R.string.game_end_positive))
                    }
                    ThemedButton(onClick = {
                        dialogHelper::triggerAnimatedDismiss.invoke()
                    }) {
                        Text(stringResource(id = R.string.game_end_negative))
                    }
                }
            }
        }
    }
}

@Preview(group = "Game Components", showBackground = true)
@Composable
fun GameContainerPreview() {
    WelcomeToFlipTheme {
        GameContainer(
            displayPosition = 1,
            displayEndPosition = 10,
            advancePosition = {},
            advancePositionEnabled = true,
            reversePosition = {},
            reversePositionEnabled = true,
            endGame = {},
            content = { modifier ->
                Text("Game content", modifier = modifier)
            },
            modifier = Modifier
        )
    }
}

@Preview(group = "Game Components", showBackground = true)
@Composable
fun EndGameDialogPreview() {
    WelcomeToFlipTheme {
        EndGameDialog(WelcomeToTheMoon, 8, reshuffleStacks = {}, endGame = {}, onDismissRequest = {})
    }
}
