package dev.veryniche.welcometoflip.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import dev.veryniche.welcometoflip.R
import dev.veryniche.welcometoflip.core.models.GameType
import dev.veryniche.welcometoflip.theme.Dimen
import dev.veryniche.welcometoflip.theme.WelcomeToFlipTheme

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
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    contentColor = MaterialTheme.colorScheme.onPrimaryContainer,
                ),
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

