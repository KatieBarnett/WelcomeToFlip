package dev.katiebarnett.welcometoflip.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.LargeTopAppBar
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.content.ContextCompat.startActivity
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.airbnb.android.showkase.models.Showkase
import dev.katiebarnett.welcometoflip.BuildConfig
import dev.katiebarnett.welcometoflip.MainViewModel
import dev.katiebarnett.welcometoflip.R
import dev.katiebarnett.welcometoflip.components.NavigationIcon
import dev.katiebarnett.welcometoflip.components.ThemedButton
import dev.katiebarnett.welcometoflip.core.models.SavedGame
import dev.katiebarnett.welcometoflip.showkase.getBrowserIntent
import dev.katiebarnett.welcometoflip.theme.Dimen
import dev.katiebarnett.welcometoflip.theme.WelcomeToFlipTheme
import dev.katiebarnett.welcometoflip.util.AboutAppText
import dev.katiebarnett.welcometoflip.util.Analytics
import dev.katiebarnett.welcometoflip.util.TrackedScreen
import dev.katiebarnett.welcometoflip.util.UnorderedListText
import dev.katiebarnett.welcometoflip.util.trackScreenView

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AboutScreen(
    navController: NavController = rememberNavController(),
    modifier: Modifier = Modifier) {
    val scrollableState = rememberScrollState()

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
        Column(
            verticalArrangement = Arrangement.spacedBy(Dimen.spacing),
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .padding(innerPadding)
                .verticalScroll(scrollableState)
                .padding(Dimen.spacingDouble)
        ) {
            Text(
                text = stringResource(id = R.string.about_subtitle),
                style = MaterialTheme.typography.headlineMedium,
                color = MaterialTheme.colorScheme.primary,
                fontWeight = FontWeight.Bold
            )
            AboutAppText()
            Text(
                text = stringResource(id = R.string.about_developer_title),
                style = MaterialTheme.typography.headlineMedium,
                color = MaterialTheme.colorScheme.primary,
                fontWeight = FontWeight.Bold
            )
            Text(text = stringResource(id = R.string.about_developer))
            Text(
                text = stringResource(id = R.string.welcome_coming_soon_title),
                style = MaterialTheme.typography.headlineMedium,
                color = MaterialTheme.colorScheme.primary,
                fontWeight = FontWeight.Bold
            )
            UnorderedListText(
                textLines = listOf(
                    R.string.welcome_coming_soon_ul_1,
                    R.string.welcome_coming_soon_ul_2
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = Dimen.spacing)
            )
            Text(
                text = stringResource(id = R.string.about_remove_ads_title),
                style = MaterialTheme.typography.headlineMedium,
                color = MaterialTheme.colorScheme.primary,
                fontWeight = FontWeight.Bold
            )
            Text(text = stringResource(id = R.string.about_remove_ads))
            Spacer(modifier = Modifier.height(Dimen.spacingDouble))
            Text(
                text = stringResource(id = R.string.about_credits_graphics_title),
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.primary,
                fontWeight = FontWeight.Bold
            )
            UnorderedListText(
                textLines = listOf(
                    R.string.about_credits_graphics_1,
                    R.string.about_credits_graphics_2,
                    R.string.about_credits_graphics_3,
                    R.string.about_credits_graphics_4,
                    R.string.about_credits_graphics_5,
                    R.string.about_credits_graphics_6,
                    R.string.about_credits_graphics_7,
                    R.string.about_credits_graphics_8,
                    R.string.about_credits_graphics_9
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = Dimen.spacing)
            )
            if (BuildConfig.DEBUG) {
                val context = LocalContext.current
                ThemedButton(content = {
                    Text(stringResource(id = R.string.debug_showkase))
                }, onClick = {
                    startActivity(context, Showkase.getBrowserIntent(context), null)
                })
            }
        }
    }
}


@Preview(group = "About Screen", showBackground = true)
@Composable
fun AboutScreenPreview() {
    WelcomeToFlipTheme {
        AboutScreen()
    }
}
