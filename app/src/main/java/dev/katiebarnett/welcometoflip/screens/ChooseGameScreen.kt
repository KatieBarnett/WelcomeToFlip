package dev.katiebarnett.welcometoflip.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import dev.katiebarnett.welcometoflip.MainViewModel
import dev.katiebarnett.welcometoflip.R
import dev.katiebarnett.welcometoflip.components.ThemedButton
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
        modifier
            .fillMaxSize()
            .padding(Dimen.spacing)
    ) {
        Text(stringResource(id = R.string.main_instruction))
        games.forEach {
            ThemedButton(onClick = { chooseGameAction.invoke(it) }) {
                GameChoice(it)
            }
        }
    }
}

@Composable
fun GameChoice(gameType: GameType, modifier: Modifier = Modifier) {
    Column {
        Box(modifier = modifier.padding(Dimen.Button.padding),
            contentAlignment = Alignment.Center) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Icon(
                    painter = painterResource(id = gameType.icon),
                    contentDescription = stringResource(id = gameType.displayName)
                )
                Text(text = stringResource(gameType.displayName), modifier.align(alignment = Alignment.CenterHorizontally))
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun GameChoicePreview() {
    WelcomeToFlipTheme {
        GameChoice(WelcomeToTheMoon, Modifier)
    }
}

@Preview(showBackground = true)
@Composable
fun GameListPreview() {
    WelcomeToFlipTheme {
        GameChoiceList(listOf(WelcomeToTheMoon), {}, Modifier)
    }
}