package dev.katiebarnett.welcometoflip.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import dev.katiebarnett.welcometoflip.MainViewModel
import dev.katiebarnett.welcometoflip.R
import dev.katiebarnett.welcometoflip.components.ButtonWithIcon
import dev.katiebarnett.welcometoflip.data.GameType
import dev.katiebarnett.welcometoflip.data.WelcomeToTheMoon
import dev.katiebarnett.welcometoflip.theme.Dimen
import dev.katiebarnett.welcometoflip.theme.WelcomeToFlipTheme

@Composable
fun ChooseGameBody(chooseGameAction: (gameType: GameType) -> Unit, modifier: Modifier = Modifier) {
    val viewModel: MainViewModel = viewModel()
    GameChoiceList(games = viewModel.games, chooseGameAction = chooseGameAction, modifier)
}

@Composable
fun GameChoiceList(games: List<GameType>, chooseGameAction: (gameType: GameType) -> Unit, modifier: Modifier = Modifier) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(Dimen.Button.spacing),
        modifier = modifier
            .fillMaxSize()
            .padding(Dimen.spacing)
    ) {
        Text(stringResource(id = R.string.main_instruction))
        games.forEach { gameType ->
            ButtonWithIcon(
                textRes = gameType.displayName,
                iconRes = gameType.icon,
                onClick = { chooseGameAction.invoke(gameType) }
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun GameListPreview() {
    WelcomeToFlipTheme {
        GameChoiceList(listOf(WelcomeToTheMoon, WelcomeToTheMoon), {}, Modifier)
    }
}