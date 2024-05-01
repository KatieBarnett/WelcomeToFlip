package dev.veryniche.welcometoflip.screens

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.LargeTopAppBar
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.content.ContextCompat.startActivity
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.airbnb.android.showkase.models.Showkase
import dev.veryniche.welcometoflip.BuildConfig
import dev.veryniche.welcometoflip.R
import dev.veryniche.welcometoflip.components.NavigationIcon
import dev.veryniche.welcometoflip.components.ThemedButton
import dev.veryniche.welcometoflip.showkase.getBrowserIntent
import dev.veryniche.welcometoflip.theme.Dimen
import dev.veryniche.welcometoflip.theme.WelcomeToFlipTheme
import dev.veryniche.welcometoflip.util.AboutAppText
import dev.veryniche.welcometoflip.util.Analytics
import dev.veryniche.welcometoflip.util.TrackedScreen
import dev.veryniche.welcometoflip.util.UnorderedListText
import dev.veryniche.welcometoflip.util.trackScreenView

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AboutScreen(
    navController: NavController = rememberNavController(),
    modifier: Modifier = Modifier) {
    val scrollableState = rememberScrollState()
    val context = LocalContext.current
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
            verticalArrangement = Arrangement.spacedBy(Dimen.spacingDouble),
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
            Text(text = stringResource(id = R.string.about_developer_text))
            val aboutDeveloperUrl = stringResource(id = R.string.about_developer_url)
            ThemedButton(content = {
                Text(text = stringResource(id = R.string.about_developer))
            }, onClick = {
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(aboutDeveloperUrl))
                context.startActivity(intent)
            })
            Text(
                text = stringResource(id = R.string.welcome_coming_soon_title),
                style = MaterialTheme.typography.headlineMedium,
                color = MaterialTheme.colorScheme.primary,
                fontWeight = FontWeight.Bold
            )
            UnorderedListText(
                textLines = listOf(
                    R.string.welcome_coming_soon_ul_1,
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
