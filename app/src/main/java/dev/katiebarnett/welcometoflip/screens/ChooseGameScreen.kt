package dev.katiebarnett.welcometoflip.screens

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import dev.katiebarnett.welcometoflip.MainViewModel
import dev.katiebarnett.welcometoflip.R
import dev.katiebarnett.welcometoflip.components.ButtonWithIcon
import dev.katiebarnett.welcometoflip.components.IconButton
import dev.katiebarnett.welcometoflip.core.models.GameType
import dev.katiebarnett.welcometoflip.core.models.SavedGame
import dev.katiebarnett.welcometoflip.core.models.WelcomeToTheMoon
import dev.katiebarnett.welcometoflip.theme.Dimen
import dev.katiebarnett.welcometoflip.theme.WelcomeToFlipTheme
import dev.katiebarnett.welcometoflip.util.displayDateTime
import dev.katiebarnett.welcometoflip.core.R as Rcore

@Composable
fun ChooseGame(chooseGameAction: (gameType: GameType) -> Unit, loadGameAction: (savedGame: SavedGame) -> Unit, modifier: Modifier = Modifier) {
    val viewModel: MainViewModel = hiltViewModel()
    val savedGames: List<SavedGame> by viewModel.savedGames.collectAsState(initial = emptyList())

    ChooseGameBody(
        gameTypes = viewModel.gameTypes,
        savedGames = savedGames,
        chooseNewGameAction = chooseGameAction,
        loadGameAction = { savedGame -> 
            loadGameAction.invoke(savedGame)
        },
        deleteSavedGameAction = { savedGame -> 
            viewModel.deleteGameAction(savedGame)
        },
        modifier = modifier
    )
}

@Composable
fun ChooseGameBody(gameTypes: List<GameType>,
                   savedGames: List<SavedGame>,
                   chooseNewGameAction: (gameType: GameType) -> Unit,
                   loadGameAction: (savedGame: SavedGame) -> Unit,
                   deleteSavedGameAction: (savedGame: SavedGame) -> Unit,
                   modifier: Modifier = Modifier) {
    
    Column(
        verticalArrangement = Arrangement.spacedBy(Dimen.spacingDouble),
        modifier = modifier.padding(Dimen.spacing)
    ) {
        GameChoiceList(gameTypes, chooseNewGameAction, Modifier)
        if (savedGames.isNotEmpty()) {
            SavedGamesList(
                savedGames, loadGameAction, deleteSavedGameAction,
                Modifier.weight(1f)
            )
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun GameChoiceList(gameTypes: List<GameType>, chooseGameAction: (gameType: GameType) -> Unit, modifier: Modifier = Modifier) {
    LazyColumn(
        horizontalAlignment = Alignment.Start,
        verticalArrangement = Arrangement.spacedBy(Dimen.Button.spacing),
        modifier = modifier
    ) {
        stickyHeader {
            Text(stringResource(id = R.string.main_instruction))
        }
        items(gameTypes) { gameType ->
            ButtonWithIcon(
                textRes = gameType.displayName,
                iconRes = gameType.icon,
                onClick = { chooseGameAction.invoke(gameType) }
            )
        }
    }
}


@OptIn(ExperimentalFoundationApi::class)
@Composable
fun SavedGamesList(savedGames: List<SavedGame>,
                   loadGameAction: (savedGame: SavedGame) -> Unit,
                   deleteGameAction: (savedGame: SavedGame) -> Unit,
                   modifier: Modifier = Modifier) {
    LazyColumn(
        horizontalAlignment = Alignment.Start,
        verticalArrangement = Arrangement.spacedBy(Dimen.Button.spacing),
        modifier = modifier
    ) {
        stickyHeader {
            Text(stringResource(id = R.string.saved_instruction))
        }
        items(savedGames) { game ->
            SavedGame(game, loadGameAction, deleteGameAction)
        }
    }
}

@Composable
fun SavedGame(savedGame: SavedGame,
              loadGameAction: (savedGame: SavedGame) -> Unit,
              deleteGameAction: (savedGame: SavedGame) -> Unit,
              modifier: Modifier = Modifier) {
    Row(verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(Dimen.spacing)) {
        savedGame.gameType?.let {
            Icon(
                painter = painterResource(id = it.icon),
                contentDescription = stringResource(id = it.displayName),
                modifier = Modifier
                    .size(Dimen.SavedGame.iconSize)
            )
        }
        Text(savedGame.lastModified.displayDateTime(stringResource(id = R.string.date_time_format)))
        Spacer(modifier = Modifier.weight(1f))
        IconButton(
            altTextRes = R.string.delete_saved_button,
            iconRes = Rcore.drawable.noun_bin_2034046,
            onClick = { deleteGameAction.invoke(savedGame) }
        )
        IconButton(
            altTextRes = R.string.load_saved_button,
            iconRes = Rcore.drawable.noun_arrow_60381,
            onClick = { loadGameAction.invoke(savedGame) }
        )
    }
}

@Preview(showBackground = true)
@Composable
fun SavedGamePreview() {
    WelcomeToFlipTheme {
        SavedGame(
            SavedGame(
                position = 1,
                seed = 1234567890,
                gameType = WelcomeToTheMoon,
                lastModified = System.currentTimeMillis()
            ), {}, {}, Modifier)
    }
}

@Preview(showBackground = true)
@Composable
fun ChooseGameBodyPreview() {
    WelcomeToFlipTheme {
        val savedGame1 = SavedGame(
            position = 1,
            seed = 1234567890,
            gameType = WelcomeToTheMoon,
            lastModified = System.currentTimeMillis()
        )
        val savedGame2 = SavedGame(
            position = 90,
            seed = 987654321,
            gameType = WelcomeToTheMoon,
            lastModified = System.currentTimeMillis()
        )
        ChooseGameBody(
            gameTypes = listOf(WelcomeToTheMoon, WelcomeToTheMoon),
            savedGames = listOf(savedGame1, savedGame2),
            chooseNewGameAction = {},
            loadGameAction = {},
            deleteSavedGameAction = {},
            modifier = Modifier
        )
    }
}

@Preview(showBackground = true)
@Composable
fun GameListPreview() {
    WelcomeToFlipTheme {
        GameChoiceList(listOf(WelcomeToTheMoon, WelcomeToTheMoon), {}, Modifier)
    }
}

@Preview(showBackground = true)
@Composable
fun SavedGameListPreview() {
    WelcomeToFlipTheme {
        val savedGame1 = SavedGame(
            position = 1,
            seed = 1234567890,
            gameType = WelcomeToTheMoon,
            lastModified = System.currentTimeMillis()
        )
        val savedGame2 = SavedGame(
            position = 90,
            seed = 987654321,
            gameType = WelcomeToTheMoon,
            lastModified = System.currentTimeMillis()
        )
        SavedGamesList(listOf(savedGame1, savedGame2), {}, {}, Modifier)
    }
}