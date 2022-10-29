package dev.katiebarnett.welcometoflip.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.LargeTopAppBar
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import dev.katiebarnett.welcometoflip.MainViewModel
import dev.katiebarnett.welcometoflip.R
import dev.katiebarnett.welcometoflip.components.NavigationIcon
import dev.katiebarnett.welcometoflip.core.models.SavedGame
import dev.katiebarnett.welcometoflip.theme.WelcomeToFlipTheme
import dev.katiebarnett.welcometoflip.util.Analytics
import dev.katiebarnett.welcometoflip.util.TrackedScreen
import dev.katiebarnett.welcometoflip.util.trackScreenView

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AboutScreen(
    navController: NavController = rememberNavController(),
    modifier: Modifier = Modifier) {

    TrackedScreen {
        trackScreenView(name = Analytics.Screen.About)
    }
    Scaffold(
        topBar = {
            LargeTopAppBar(
                title = { Text(text = stringResource(id = R.string.app_name))},
                navigationIcon = { NavigationIcon(navController = navController)}
            )
        },
        modifier = modifier
    ) { innerPadding ->
        Text("About", Modifier.padding(innerPadding))
    }
}


@Preview(showBackground = true)
@Composable
fun AboutScreenPreview() {
    WelcomeToFlipTheme {
        AboutScreen()
    }
}
