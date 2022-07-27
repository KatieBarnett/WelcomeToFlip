package dev.katiebarnett.welcometoflip.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import com.google.android.material.composethemeadapter.MdcTheme
import dev.katiebarnett.welcometoflip.MainViewModel
import dev.katiebarnett.welcometoflip.R
import dev.katiebarnett.welcometoflip.data.GameType
import dev.katiebarnett.welcometoflip.data.WelcomeToTheMoon

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
            .padding(dimensionResource(id = R.dimen.spacing))
    ) {
        Text(stringResource(id = R.string.main_instruction))
        games.forEach {
            Button(onClick = { chooseGameAction.invoke(it) },
                colors = ButtonDefaults.buttonColors(containerColor = colorResource(id = R.color.button_background))
            ) {
                GameChoice(it)
            }
        }
    }
}

@Composable
fun GameChoice(gameType: GameType, modifier: Modifier = Modifier) {
    Column {
        Box(modifier = modifier.padding(dimensionResource(id = R.dimen.button_padding)),
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
    MdcTheme {
        GameChoice(WelcomeToTheMoon, Modifier)
    }
}

@Preview(showBackground = true)
@Composable
fun GameListPreview() {
    MdcTheme {
        GameChoiceList(listOf(WelcomeToTheMoon), {}, Modifier)
    }
}