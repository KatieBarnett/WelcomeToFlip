package dev.veryniche.welcometoflip.components

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
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
fun DialogButton(
    @StringRes textRes: Int,
    onClick: () -> Unit,
) {
    ThemedButton(onClick = onClick) {
        Text(
            text = stringResource(id = textRes),
            style = MaterialTheme.typography.headlineSmall
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
        EndGameDialogContent(
            gameType = gameType,
            position = position,
            reshuffleStacks = reshuffleStacks,
            endGame = endGame,
            triggerAnimatedDismiss = dialogHelper::triggerAnimatedDismiss
        )
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun EndGameDialogContent(
    gameType: GameType,
    position: Int,
    reshuffleStacks: () -> Unit,
    endGame: () -> Unit,
    triggerAnimatedDismiss: () -> Unit
) {
    Surface(
        shape = RoundedCornerShape(Dimen.Dialog.radius),
        modifier = Modifier
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(Dimen.spacingDouble),
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.padding(Dimen.spacingDouble)
        ) {
            Text(
                text = stringResource(id = R.string.stack_end_message),
                style = MaterialTheme.typography.headlineMedium
            )
            FlowRow(
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalArrangement = Arrangement.spacedBy(Dimen.spacing),
                modifier = Modifier.fillMaxWidth()
            ) {
                DialogButton(
                    textRes = R.string.reshuffle_button,
                    onClick = {
                        trackShuffleGame(gameType, position)
                        reshuffleStacks.invoke()
                        triggerAnimatedDismiss.invoke()
                    }
                )
                DialogButton(
                    textRes = R.string.end_game_button,
                    onClick = {
                        trackEndGame(gameType, position)
                        triggerAnimatedDismiss.invoke()
                        endGame.invoke()
                    }
                )
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
        EndGameConfirmationDialogContent(
            gameType = gameType,
            position = position,
            endGame = endGame,
            triggerAnimatedDismiss = dialogHelper::triggerAnimatedDismiss
        )
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun EndGameConfirmationDialogContent(
    gameType: GameType,
    position: Int,
    endGame: () -> Unit,
    triggerAnimatedDismiss: () -> Unit
) {
    Surface(
        shape = RoundedCornerShape(Dimen.Dialog.radius),
        modifier = Modifier
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(Dimen.spacingDouble),
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.padding(Dimen.spacingDouble)
        ) {
            Text(
                text = stringResource(id = R.string.game_end_confirmation_message),
                style = MaterialTheme.typography.headlineMedium
            )
            FlowRow(
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalArrangement = Arrangement.spacedBy(Dimen.spacing),
                modifier = Modifier.fillMaxWidth()
            ) {
                DialogButton(
                    textRes = R.string.game_end_positive,
                    onClick = {
                        trackEndGame(gameType, position)
                        triggerAnimatedDismiss.invoke()
                        endGame.invoke()
                    }
                )
                DialogButton(
                    textRes = R.string.game_end_negative,
                    onClick = {
                        triggerAnimatedDismiss.invoke()
                    }
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun EndGameDialogContentPreview() {
    WelcomeToFlipTheme {
        EndGameDialogContent(
            gameType = WelcomeToTheMoon,
            position = 8,
            reshuffleStacks = {},
            endGame = {},
            triggerAnimatedDismiss = {}
        )
    }
}

@Preview(showBackground = true)
@Composable
fun EndGameDialogConfirmationContentPreview() {
    WelcomeToFlipTheme {
        EndGameConfirmationDialogContent(
            gameType = WelcomeToTheMoon,
            position = 8,
            endGame = {},
            triggerAnimatedDismiss = {}
        )
    }
}
