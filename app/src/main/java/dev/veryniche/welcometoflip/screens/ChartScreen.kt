package dev.veryniche.welcometoflip.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import dev.veryniche.welcometoflip.R
import dev.veryniche.welcometoflip.components.CloseIcon
import dev.veryniche.welcometoflip.components.DeckChart
import dev.veryniche.welcometoflip.core.models.Card
import dev.veryniche.welcometoflip.core.models.GameType
import dev.veryniche.welcometoflip.core.models.WelcomeToClassic
import dev.veryniche.welcometoflip.core.models.WelcomeToTheMoon
import dev.veryniche.welcometoflip.core.models.welcomeToClassicDeck
import dev.veryniche.welcometoflip.core.models.welcomeToTheMoonDeck
import dev.veryniche.welcometoflip.theme.Dimen
import dev.veryniche.welcometoflip.theme.WelcomeToFlipTheme
import dev.veryniche.welcometoflip.util.Analytics
import dev.veryniche.welcometoflip.util.TrackedScreen
import dev.veryniche.welcometoflip.util.getTopAppBarColors
import dev.veryniche.welcometoflip.util.trackScreenView

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChartScreen(
    navController: NavController = rememberNavController(),
    gameType: GameType,
    deck: List<Card>,
    snackbarHostState: SnackbarHostState,
    modifier: Modifier = Modifier,
) {
    TrackedScreen {
        trackScreenView(name = Analytics.Screen.Charts)
    }
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = stringResource(
                            id = R.string.chart_screen_title,
                            stringResource(id = gameType.displayName)
                        )
                    )
                },
                navigationIcon = { CloseIcon(navController = navController) },
                colors = getTopAppBarColors(),
            )
        },
        snackbarHost = {
            SnackbarHost(hostState = snackbarHostState)
        },
        modifier = modifier
    ) { innerPadding ->
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxSize()
                .padding(
                    top = innerPadding.calculateTopPadding(),
                    start = innerPadding.calculateStartPadding(LayoutDirection.Ltr),
                    end = innerPadding.calculateEndPadding(LayoutDirection.Ltr),
                    bottom = 0.dp
                )
        ) {
            Text(
                text = stringResource(
                    id = R.string.chart_screen_info,
                    stringResource(id = gameType.actionCardName),
                    stringResource(id = gameType.displayName)
                ),
                style = MaterialTheme.typography.bodyLarge,
                modifier = modifier
                    .fillMaxWidth()
                    .padding(top = Dimen.spacingDouble, start = Dimen.spacingDouble, end = Dimen.spacingDouble)
            )
            DeckChart(
                deck = deck,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}

data class GameConfig(val gameType: GameType, val gameDeck: List<Card>)

class ChartScreenPreviewParameterProvider : PreviewParameterProvider<GameConfig> {
    override val values: Sequence<GameConfig> = sequenceOf(
        GameConfig(WelcomeToClassic, welcomeToClassicDeck),
        GameConfig(WelcomeToTheMoon, welcomeToTheMoonDeck),
    )
}

@Preview(showBackground = true)
@Preview(showBackground = true, device = Devices.TABLET)
@Preview(showBackground = true, device = "spec:id=reference_tablet,shape=Normal,width=800,height=1280,unit=dp,dpi=240")
@Composable
fun ChartScreenPreview(
    @PreviewParameter(ChartScreenPreviewParameterProvider::class) data: GameConfig
) {
    WelcomeToFlipTheme {
        ChartScreen(
            gameType = data.gameType,
            deck = data.gameDeck,
            snackbarHostState = remember { SnackbarHostState() }
        )
    }
}
