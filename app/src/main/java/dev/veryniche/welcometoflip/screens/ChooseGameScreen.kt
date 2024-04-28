package dev.veryniche.welcometoflip.screens

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.LargeTopAppBar
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import dev.veryniche.welcometoflip.Game
import dev.veryniche.welcometoflip.MainViewModel
import dev.veryniche.welcometoflip.R
import dev.veryniche.welcometoflip.components.AboutActionIcon
import dev.veryniche.welcometoflip.components.GameTile
import dev.veryniche.welcometoflip.components.NavigationIcon
import dev.veryniche.welcometoflip.components.ThemedIconButton
import dev.veryniche.welcometoflip.core.models.GameType
import dev.veryniche.welcometoflip.core.models.SavedGame
import dev.veryniche.welcometoflip.core.models.WelcomeToTheMoon
import dev.veryniche.welcometoflip.storage.models.game
import dev.veryniche.welcometoflip.theme.Dimen
import dev.veryniche.welcometoflip.theme.Dimen.AppBar.CollapsedTextSize
import dev.veryniche.welcometoflip.theme.Dimen.AppBar.ExpandedTextSize
import dev.veryniche.welcometoflip.theme.WelcomeToFlipTheme
import dev.veryniche.welcometoflip.util.Analytics
import dev.veryniche.welcometoflip.util.TrackedScreen
import dev.veryniche.welcometoflip.util.displayDateTime
import dev.veryniche.welcometoflip.util.trackDeleteGame
import dev.veryniche.welcometoflip.util.trackLoadGame
import dev.veryniche.welcometoflip.util.trackScreenView
import dev.veryniche.welcometoflip.core.R as Rcore

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChooseGameScreen(
    navController: NavController = rememberNavController(),
    viewModel: MainViewModel,
    modifier: Modifier = Modifier
) {
    val savedGames: List<SavedGame> by viewModel.savedGames.collectAsState(initial = emptyList())
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior(rememberTopAppBarState())

    TrackedScreen {
        trackScreenView(name = Analytics.Screen.ChooseGame)
    }

    val topAppBarElementColor = if (scrollBehavior.state.collapsedFraction > 0.5) {
        MaterialTheme.colorScheme.onSurface
    } else {
        MaterialTheme.colorScheme.onPrimary
    }
    val topAppBarTextSize = (CollapsedTextSize + (ExpandedTextSize - CollapsedTextSize) * (1 - scrollBehavior.state.collapsedFraction))

    Scaffold(
        topBar = {
            LargeTopAppBar(
                title = { Text(text = stringResource(id = R.string.app_name), fontSize = topAppBarTextSize.sp) },
                navigationIcon = { NavigationIcon(navController = navController) },
                actions = { AboutActionIcon(navController) },
                scrollBehavior = scrollBehavior,
                colors = TopAppBarDefaults.largeTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    scrolledContainerColor = MaterialTheme.colorScheme.surface,
                    navigationIconContentColor = topAppBarElementColor,
                    titleContentColor = topAppBarElementColor,
                    actionIconContentColor = topAppBarElementColor,
                )
            )
        },
        modifier = modifier.nestedScroll(scrollBehavior.nestedScrollConnection)
    ) { innerPadding ->
        ChooseGameBody(
            gameTypes = viewModel.gameTypes,
            savedGames = savedGames,
            chooseNewGameAction = { gameType, solo ->
                navController.navigate(route = Game.getRoute(gameType, solo))
            },
            loadGameAction = { savedGame ->
                trackLoadGame(savedGame)
                navController.navigate(route = Game.getRoute(savedGame))
            },
            deleteSavedGameAction = { savedGame ->
                trackDeleteGame(savedGame)
                viewModel.deleteGameAction(savedGame)
            },
            purchaseNewGameAction = { gameType, solo ->
                // TODO
            },
            modifier = modifier.padding(innerPadding)
        )
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ChooseGameBody(
    gameTypes: List<GameType>,
    savedGames: List<SavedGame>,
    chooseNewGameAction: (gameType: GameType, solo: Boolean) -> Unit,
    purchaseNewGameAction: (gameType: GameType, solo: Boolean) -> Unit,
    loadGameAction: (savedGame: SavedGame) -> Unit,
    deleteSavedGameAction: (savedGame: SavedGame) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        horizontalAlignment = Alignment.Start,
        verticalArrangement = Arrangement.spacedBy(Dimen.spacing),
        modifier = modifier.padding(Dimen.spacing)
    ) {
        item {
            Text(stringResource(id = R.string.main_instruction))
        }
        items(gameTypes) { gameType ->
            GameTile(
                textRes = gameType.displayName,
                imageRes = gameType.largeIcon,
                purchased = gameType.purchased ?: false,
                purchasePrice = gameType.purchasePrice,
                solo = gameType.solo,
                soloPurchased = gameType.soloPurchased ?: false,
                soloPurchasePrice = gameType.soloPurchasePrice,
                onClick = { solo ->
                    val isPurchased = if (solo) {
                        gameType.soloPurchased == true
                    } else {
                        gameType.purchased == true
                    }
                    if (isPurchased) {
                        chooseNewGameAction.invoke(gameType, solo)
                    } else {
                        purchaseNewGameAction.invoke(gameType, solo)
                    }
                }

            )
        }
        if (savedGames.isNotEmpty()) {
            item {
                Text(stringResource(id = R.string.saved_instruction))
            }
            items(savedGames) { game ->
                SavedGame(game, loadGameAction, deleteSavedGameAction)
            }
        }
    }
}

@Composable
fun SavedGame(
    savedGame: SavedGame,
    loadGameAction: (savedGame: SavedGame) -> Unit,
    deleteGameAction: (savedGame: SavedGame) -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(Dimen.spacing),
        modifier = modifier.padding(horizontal = Dimen.spacing)
    ) {
        savedGame.gameType?.let {
            Icon(
                painter = painterResource(id = it.icon),
                contentDescription = stringResource(id = it.displayName),
                modifier = Modifier
                    .size(Dimen.SavedGame.iconSize)
            )
        }
        Column {
            Text(savedGame.lastModified.displayDateTime(stringResource(id = R.string.date_time_format)))
            Text(stringResource(id = R.string.position_label, savedGame.displayPosition, savedGame.stackSize))
        }
        Spacer(modifier = Modifier.weight(1f))
        ThemedIconButton(
            altTextRes = R.string.delete_saved_button,
            iconRes = Rcore.drawable.noun_bin_2034046,
            onClick = { deleteGameAction.invoke(savedGame) }
        )
        ThemedIconButton(
            altTextRes = R.string.load_saved_button,
            iconRes = Rcore.drawable.noun_arrow_60381,
            onClick = { loadGameAction.invoke(savedGame) }
        )
    }
}

@Preview(group = "Choose Game Screen", showBackground = true)
@Composable
fun SavedGamePreview() {
    WelcomeToFlipTheme {
        SavedGame(
            savedGame = SavedGame(
                position = 1,
                seed = 1234567890,
                gameType = WelcomeToTheMoon,
                lastModified = System.currentTimeMillis(),
                solo = false,
                stackSize = 21
            ),
            loadGameAction = {},
            deleteGameAction = {},
            modifier = Modifier
        )
    }
}

@Preview(group = "Choose Game Screen", showBackground = true)
@Composable
fun SavedGamePreviewSolo() {
    WelcomeToFlipTheme {
        SavedGame(
            savedGame = SavedGame(
                position = 1,
                seed = 1234567890,
                gameType = WelcomeToTheMoon,
                lastModified = System.currentTimeMillis(),
                solo = true,
                stackSize = 21
            ),
            loadGameAction = {},
            deleteGameAction = {},
            modifier = Modifier
        )
    }
}

@Preview(group = "Choose Game Screen", showBackground = true)
@Composable
fun ChooseGameBodyPreview() {
    WelcomeToFlipTheme {
        val savedGame1 = SavedGame(
            position = 1,
            seed = 1234567890,
            gameType = WelcomeToTheMoon,
            lastModified = System.currentTimeMillis(),
            solo = false,
            stackSize = 21
        )
        val savedGame2 = SavedGame(
            position = 90,
            seed = 987654321,
            gameType = WelcomeToTheMoon,
            lastModified = System.currentTimeMillis(),
            solo = true,
            stackSize = 21
        )
        ChooseGameBody(
            gameTypes = listOf(WelcomeToTheMoon, WelcomeToTheMoon),
            savedGames = listOf(savedGame1, savedGame2),
            chooseNewGameAction = { gameType, solo -> },
            loadGameAction = {},
            deleteSavedGameAction = {},
            purchaseNewGameAction = { gameType, solo -> },
            modifier = Modifier
        )
    }
}
