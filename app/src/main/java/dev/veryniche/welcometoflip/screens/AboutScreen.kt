package dev.veryniche.welcometoflip.screens

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MediumTopAppBar
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.adaptive.ExperimentalMaterial3AdaptiveApi
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat.startActivity
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.airbnb.android.showkase.models.Showkase
import dev.veryniche.welcometoflip.BuildConfig
import dev.veryniche.welcometoflip.R
import dev.veryniche.welcometoflip.components.NavigationIcon
import dev.veryniche.welcometoflip.components.ShopActionIcon
import dev.veryniche.welcometoflip.components.ThemedButton
import dev.veryniche.welcometoflip.previews.getPreviewWindowSizeClass
import dev.veryniche.welcometoflip.purchase.InAppProduct
import dev.veryniche.welcometoflip.purchase.Products
import dev.veryniche.welcometoflip.showkase.getBrowserIntent
import dev.veryniche.welcometoflip.theme.Dimen
import dev.veryniche.welcometoflip.theme.WelcomeToFlipTheme
import dev.veryniche.welcometoflip.util.AboutAppText
import dev.veryniche.welcometoflip.util.Analytics
import dev.veryniche.welcometoflip.util.ImageCreditText
import dev.veryniche.welcometoflip.util.TrackedScreen
import dev.veryniche.welcometoflip.util.UnorderedListText
import dev.veryniche.welcometoflip.util.getMediumTopAppBarColors
import dev.veryniche.welcometoflip.util.getTopAppBarTextSize
import dev.veryniche.welcometoflip.util.trackScreenView

@Composable
fun AboutHeading(textRes: Int, modifier: Modifier = Modifier) {
    Text(
        text = stringResource(id = textRes),
        style = MaterialTheme.typography.headlineSmall,
        color = MaterialTheme.colorScheme.secondary,
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
    showShopMenuItem: Boolean,
    purchaseStatus: Map<String, InAppProduct>,
    onPurchaseClick: (String) -> Unit,
    modifier: Modifier = Modifier,
    windowSizeClass: WindowSizeClass,
) {
    val scrollableState = rememberScrollState()
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior(rememberTopAppBarState())
    val topAppBarTextSize = getTopAppBarTextSize(scrollBehavior.state.collapsedFraction)

    val context = LocalContext.current
    TrackedScreen {
        trackScreenView(name = Analytics.Screen.About)
    }
    Scaffold(
        topBar = {
            MediumTopAppBar(
                title = {
                    Text(
                        text = stringResource(id = R.string.app_name),
                        fontSize = topAppBarTextSize.sp
                    )
                },
                navigationIcon = { NavigationIcon(navController = navController) },
                actions = {
                    if (showShopMenuItem) {
                        ShopActionIcon(navController = navController)
                    }
                },
                scrollBehavior = scrollBehavior,
                colors = getMediumTopAppBarColors()
            )
        },
        modifier = modifier.nestedScroll(scrollBehavior.nestedScrollConnection)
    ) { innerPadding ->
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier.fillMaxWidth()
                .padding(
                    top = innerPadding.calculateTopPadding(),
                    start = innerPadding.calculateStartPadding(LayoutDirection.Ltr),
                    end = innerPadding.calculateEndPadding(LayoutDirection.Ltr),
                    bottom = 0.dp
                )
        ) {
            Column(
                verticalArrangement = Arrangement.spacedBy(Dimen.spacingDouble),
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .verticalScroll(scrollableState)
                    .padding(Dimen.spacingDouble)
                    .widthIn(max = 500.dp)
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
                    if (it.purchased != true && purchaseStatus[Products.bundle]?.purchased != true) {
                        AboutHeading(R.string.about_remove_ads_title)
                        AboutText(R.string.about_remove_ads_text)
                        ThemedButton(content = {
                            Text(
                                text = stringResource(
                                    id = R.string.about_remove_ads,
                                    it.displayedPrice
                                )
                            )
                        }, onClick = {
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

                val privacyPolicyUrl = stringResource(id = R.string.about_privacy_policy_url)
                AboutHeading(R.string.about_privacy_policy_title)
                Button(content = {
                    Text(text = stringResource(id = R.string.about_privacy_policy))
                }, onClick = {
                    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(privacyPolicyUrl))
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
                    text = stringResource(
                        id = R.string.about_version,
                        BuildConfig.VERSION_NAME,
                        BuildConfig.VERSION_CODE
                    ),
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.primary,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center,
                    modifier = modifier.fillMaxWidth()
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3AdaptiveApi::class)
@Preview(showBackground = true)
@Preview(showBackground = true, device = Devices.TABLET)
@Preview(showBackground = true, device = "spec:id=reference_tablet,shape=Normal,width=800,height=1280,unit=dp,dpi=240")
@Composable
fun AboutScreenPreview() {
    WelcomeToFlipTheme {
        AboutScreen(
            showShopMenuItem = true,
            purchaseStatus = mapOf(
                Pair(
                    Products.adRemoval,
                    InAppProduct(Products.adRemoval, "Ad Removal", "Ad Removal", "1.00", "AUD", false)
                )
            ),
            onPurchaseClick = {},
            windowSizeClass = getPreviewWindowSizeClass()
        )
    }
}

@OptIn(ExperimentalMaterial3AdaptiveApi::class)
@Preview(showBackground = true)
@Preview(showBackground = true, device = Devices.TABLET)
@Preview(showBackground = true, device = "spec:id=reference_tablet,shape=Normal,width=800,height=1280,unit=dp,dpi=240")
@Composable
fun AboutScreenPurchasedPreview() {
    WelcomeToFlipTheme {
        AboutScreen(
            showShopMenuItem = false,
            purchaseStatus = mapOf(
                Pair(
                    Products.adRemoval,
                    InAppProduct(Products.adRemoval, "Ad Removal", "Ad Removal", "1.00", "AUD", true)
                )
            ),
            onPurchaseClick = {},
            windowSizeClass = getPreviewWindowSizeClass()
        )
    }
}
