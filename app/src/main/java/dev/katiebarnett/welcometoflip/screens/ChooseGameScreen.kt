package dev.katiebarnett.welcometoflip.screens

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
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
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import dev.katiebarnett.welcometoflip.Game
import dev.katiebarnett.welcometoflip.MainViewModel
import dev.katiebarnett.welcometoflip.R
import dev.katiebarnett.welcometoflip.components.AboutActionIcon
import dev.katiebarnett.welcometoflip.components.ButtonWithIcon
import dev.katiebarnett.welcometoflip.components.NavigationIcon
import dev.katiebarnett.welcometoflip.components.ThemedIconButton
import dev.katiebarnett.welcometoflip.core.models.GameType
import dev.katiebarnett.welcometoflip.core.models.SavedGame
import dev.katiebarnett.welcometoflip.core.models.WelcomeToTheMoon
import dev.katiebarnett.welcometoflip.theme.Dimen
import dev.katiebarnett.welcometoflip.theme.Dimen.AppBar.CollapsedTextSize
import dev.katiebarnett.welcometoflip.theme.Dimen.AppBar.ExpandedTextSize
import dev.katiebarnett.welcometoflip.theme.WelcomeToFlipTheme
import dev.katiebarnett.welcometoflip.util.Analytics
import dev.katiebarnett.welcometoflip.util.TrackedScreen
import dev.katiebarnett.welcometoflip.util.displayDateTime
import dev.katiebarnett.welcometoflip.util.trackDeleteGame
import dev.katiebarnett.welcometoflip.util.trackLoadGame
import dev.katiebarnett.welcometoflip.util.trackScreenView
import dev.katiebarnett.welcometoflip.core.R as Rcore

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChooseGameScreen(
    navController: NavController = rememberNavController(),
    modifier: Modifier = Modifier) {

    val viewModel: MainViewModel = hiltViewModel()
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
    val topAppBarTextSize = (CollapsedTextSize + (ExpandedTextSize - CollapsedTextSize)*(1-scrollBehavior.state.collapsedFraction))

    Scaffold(
        topBar = {
            LargeTopAppBar(
                title = { Text(text = stringResource(id = R.string.app_name), fontSize = topAppBarTextSize.sp)},
                navigationIcon = { NavigationIcon(navController = navController)},
                actions = { AboutActionIcon(navController) },
                scrollBehavior = scrollBehavior,
                colors = TopAppBarDefaults.largeTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    scrolledContainerColor = MaterialTheme.colorScheme.surface,
                    navigationIconContentColor = topAppBarElementColor,
                    titleContentColor = topAppBarElementColor,
                    actionIconContentColor= topAppBarElementColor,
                )
            )
        },
        modifier = modifier.nestedScroll(scrollBehavior.nestedScrollConnection)
    ) { innerPadding ->
        ChooseGameBody(
            gameTypes = viewModel.gameTypes,
            savedGames = savedGames,
            chooseNewGameAction = {
                navController.navigate(route = Game.getRoute(it))
            },
            loadGameAction = { savedGame ->
                trackLoadGame(savedGame)
                navController.navigate(route = Game.getRoute(savedGame))
            },
            deleteSavedGameAction = { savedGame ->
                trackDeleteGame(savedGame)
                viewModel.deleteGameAction(savedGame)
            },
            modifier = modifier.padding(innerPadding)
        )
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ChooseGameBody(gameTypes: List<GameType>,
                   savedGames: List<SavedGame>,
                   chooseNewGameAction: (gameType: GameType) -> Unit,
                   loadGameAction: (savedGame: SavedGame) -> Unit,
                   deleteSavedGameAction: (savedGame: SavedGame) -> Unit,
                   modifier: Modifier = Modifier) {

    LazyColumn(
        horizontalAlignment = Alignment.Start,
        verticalArrangement = Arrangement.spacedBy(Dimen.spacing),
        modifier = modifier
    ) {
        stickyHeader {
            Column(verticalArrangement = Arrangement.spacedBy(Dimen.spacing),
                modifier = Modifier
                    .fillMaxWidth()
                    .background(MaterialTheme.colorScheme.background)
                    .padding(Dimen.spacing)
            ) {
                Text(stringResource(id = R.string.main_instruction))
                gameTypes.forEach { gameType ->
                    ButtonWithIcon(
                        textRes = gameType.displayName,
                        iconRes = gameType.icon,
                        onClick = { chooseNewGameAction.invoke(gameType) }
                    )
                }
                Text(stringResource(id = R.string.saved_instruction))
            }
        }
        if (savedGames.isNotEmpty()) {
            items(savedGames) { game ->
                SavedGame(game, loadGameAction, deleteSavedGameAction)
            }
        }
    }
}


@Composable
fun SavedGame(savedGame: SavedGame,
              loadGameAction: (savedGame: SavedGame) -> Unit,
              deleteGameAction: (savedGame: SavedGame) -> Unit,
              modifier: Modifier = Modifier) {
    Row(verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(Dimen.spacing),
        modifier = modifier.padding(horizontal = Dimen.spacing)) {
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

@Preview(showBackground = true)
@Composable
fun SavedGamePreview() {
    WelcomeToFlipTheme {
        SavedGame(
            SavedGame(
                position = 1,
                seed = 1234567890,
                gameType = WelcomeToTheMoon,
                lastModified = System.currentTimeMillis(),
                stackSize = 21
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
            lastModified = System.currentTimeMillis(),
            stackSize = 21
        )
        val savedGame2 = SavedGame(
            position = 90,
            seed = 987654321,
            gameType = WelcomeToTheMoon,
            lastModified = System.currentTimeMillis(),
            stackSize = 21
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