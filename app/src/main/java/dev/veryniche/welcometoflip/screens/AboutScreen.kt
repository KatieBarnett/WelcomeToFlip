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
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.content.ContextCompat.startActivity
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.airbnb.android.showkase.models.Showkase
import dev.veryniche.welcometoflip.BuildConfig
import dev.veryniche.welcometoflip.R
import dev.veryniche.welcometoflip.components.NavigationIcon
import dev.veryniche.welcometoflip.components.ThemedButton
import dev.veryniche.welcometoflip.purchase.Products
import dev.veryniche.welcometoflip.purchase.PurchaseStatus
import dev.veryniche.welcometoflip.showkase.getBrowserIntent
import dev.veryniche.welcometoflip.theme.Dimen
import dev.veryniche.welcometoflip.theme.WelcomeToFlipTheme
import dev.veryniche.welcometoflip.util.AboutAppText
import dev.veryniche.welcometoflip.util.Analytics
import dev.veryniche.welcometoflip.util.ImageCreditText
import dev.veryniche.welcometoflip.util.TrackedScreen
import dev.veryniche.welcometoflip.util.UnorderedListText
import dev.veryniche.welcometoflip.util.trackPurchaseClick
import dev.veryniche.welcometoflip.util.trackScreenView

@Composable
fun AboutHeading(textRes: Int, modifier: Modifier = Modifier) {
    Text(
        text = stringResource(id = textRes),
        style = MaterialTheme.typography.headlineSmall,
        color = MaterialTheme.colorScheme.primary,
        fontWeight = FontWeight.Bold,
        modifier = modifier.fillMaxWidth()
    )
}

@Composable
fun AboutText(textRes: Int, modifier: Modifier = Modifier) {
    Text(
        text = stringResource(id = textRes),
        style = MaterialTheme.typography.bodyLarge,
        modifier = modifier.fillMaxWidth()
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AboutScreen(
    navController: NavController = rememberNavController(),
    purchaseStatus: Map<String, PurchaseStatus>,
    onPurchaseClick: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val scrollableState = rememberScrollState()
    val context = LocalContext.current
    TrackedScreen {
        trackScreenView(name = Analytics.Screen.About)
    }
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = stringResource(id = R.string.app_name)) },
                navigationIcon = { NavigationIcon(navController = navController) }
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
            AboutHeading(R.string.about_subtitle)
            AboutAppText()
            AboutHeading(R.string.welcome_coming_soon_title)
            UnorderedListText(
                textLines = listOf(
                    R.string.welcome_coming_soon_ul_1,
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = Dimen.spacing)
            )
            purchaseStatus[Products.adRemoval]?.let {
                if (it.purchased) {
                    AboutHeading(R.string.about_remove_ads_title)
                    ThemedButton(content = {
                        Text(text = stringResource(id = R.string.about_remove_ads, it.purchasePrice))
                    }, onClick = {
                        trackPurchaseClick(it.productId)
                        onPurchaseClick.invoke(it.productId)
                    })
                }
            }
            AboutHeading(R.string.about_developer_title)
            AboutText(R.string.about_developer_text)
            val aboutDeveloperUrl = stringResource(id = R.string.about_developer_url)
            ThemedButton(content = {
                Text(text = stringResource(id = R.string.about_developer))
            }, onClick = {
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(aboutDeveloperUrl))
                context.startActivity(intent)
            })
            Spacer(modifier = Modifier.height(Dimen.spacingDouble))
            AboutText(R.string.about_credits_graphics_title)
            ImageCreditText(Modifier)
            if (BuildConfig.DEBUG) {
                ThemedButton(content = {
                    Text(stringResource(id = R.string.debug_showkase))
                }, onClick = {
                    startActivity(context, Showkase.getBrowserIntent(context), null)
                })
            }
            Spacer(modifier = Modifier.height(Dimen.spacingDouble))
            Text(
                text = stringResource(id = R.string.about_version, BuildConfig.VERSION_NAME, BuildConfig.VERSION_CODE),
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.primary,
                textAlign = TextAlign.Center,
                modifier = modifier.fillMaxWidth()
            )
        }
    }
}

@Preview(group = "About Screen", showBackground = true)
@Composable
fun AboutScreenPreview() {
    WelcomeToFlipTheme {
        AboutScreen(
            purchaseStatus = mapOf(
                Pair(Products.adRemoval, PurchaseStatus(Products.adRemoval, false, "$1.00"))
            ),
            onPurchaseClick = {}
        )
    }
}

@Preview(group = "About Screen", showBackground = true)
@Composable
fun AboutScreenPurchasedPreview() {
    WelcomeToFlipTheme {
        AboutScreen(
            purchaseStatus = mapOf(
                Pair(Products.adRemoval, PurchaseStatus(Products.adRemoval, true, "$1.00"))
            ),
            onPurchaseClick = {}
        )
    }
}
