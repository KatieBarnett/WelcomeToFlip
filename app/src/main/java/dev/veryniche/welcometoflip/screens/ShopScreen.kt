package dev.veryniche.welcometoflip.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.adaptive.ExperimentalMaterial3AdaptiveApi
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import dev.veryniche.welcometoflip.R
import dev.veryniche.welcometoflip.core.models.WelcomeToClassic
import dev.veryniche.welcometoflip.core.models.WelcomeToTheMoon
import dev.veryniche.welcometoflip.previews.getPreviewWindowSizeClass
import dev.veryniche.welcometoflip.purchase.InAppProduct
import dev.veryniche.welcometoflip.purchase.Products
import dev.veryniche.welcometoflip.purchase.getLargeIcon
import dev.veryniche.welcometoflip.purchase.mapToProductId
import dev.veryniche.welcometoflip.purchase.multiplayerGameIds
import dev.veryniche.welcometoflip.theme.Dimen
import dev.veryniche.welcometoflip.theme.WelcomeToFlipTheme
import dev.veryniche.welcometoflip.util.Analytics
import dev.veryniche.welcometoflip.util.CollapsingTopAppBar
import dev.veryniche.welcometoflip.util.TrackedScreen
import dev.veryniche.welcometoflip.util.trackScreenView

@Composable
fun ShopHeading(textRes: Int, modifier: Modifier = Modifier) {
    Text(
        text = stringResource(id = textRes),
        style = MaterialTheme.typography.headlineSmall,
        color = MaterialTheme.colorScheme.secondary,
        fontWeight = FontWeight.Bold,
        modifier = modifier.fillMaxWidth()
    )
}

@Composable
fun ShopText(textRes: Int, modifier: Modifier = Modifier) {
    Text(
        text = stringResource(id = textRes),
        style = MaterialTheme.typography.bodyLarge,
        modifier = modifier.fillMaxWidth()
    )
}

@Composable
fun ShopItem(product: InAppProduct, onPurchaseClick: (String) -> Unit, modifier: Modifier = Modifier) {
    val icon = product.productId.getLargeIcon()
    ElevatedCard(
        elevation = CardDefaults.cardElevation(
            defaultElevation = 10.dp
        ),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primary,
        ),
        modifier = modifier
            .height(IntrinsicSize.Min)
            .fillMaxWidth()
            .clickable {
                onPurchaseClick.invoke(product.productId)
            }
    ) {
        Row(modifier = Modifier.height(IntrinsicSize.Min)) {
            Column(
                Modifier
                    .weight(1f)
                    .padding(Dimen.spacingDouble)
            ) {
                Text(
                    text = product.productName,
                    style = MaterialTheme.typography.headlineSmall,
                    color = MaterialTheme.colorScheme.onPrimary,
                    modifier = Modifier
                )
                Text(
                    text = product.productDescription,
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onPrimary,
                    textAlign = TextAlign.Left,
                    modifier = Modifier
                )
                Text(
                    text = stringResource(id = R.string.shop_purchase, product.displayedPrice),
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onPrimary,
                    modifier = Modifier.padding(top = Dimen.spacingDouble)
                )
            }
            icon?.let {
                Box(
                    Modifier
                        .weight(0.40f)
                        .fillMaxHeight()
                ) {
                    Image(
                        painter = painterResource(id = icon),
                        contentDescription = product.productName,
                        colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.onPrimary),
                        contentScale = ContentScale.Inside,
                        modifier = Modifier
                            .alpha(0.4f)
                            .fillMaxHeight()
                            .padding(end = Dimen.spacingDouble)
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ShopScreen(
    navController: NavController = rememberNavController(),
    purchaseStatus: Map<String, InAppProduct>,
    onPurchaseClick: (String) -> Unit,
    modifier: Modifier = Modifier,
    windowSizeClass: WindowSizeClass,
) {
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior(rememberTopAppBarState())
    TrackedScreen {
        trackScreenView(name = Analytics.Screen.Shop)
    }
    Scaffold(
        topBar = {
            CollapsingTopAppBar(
                titleRes = R.string.shop_title,
                navController = navController,
                scrollBehavior = scrollBehavior,
                showShopMenuItem = false,
                showAboutMenuItem = true,
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
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(Dimen.spacingDouble),
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .padding(Dimen.spacingDouble)
                    .widthIn(max = 500.dp)
            ) {
                item {
                    ShopText(textRes = R.string.shop_text)
                }
                if (purchaseStatus.any { multiplayerGameIds.contains(it.key) && it.value.purchased != true }) {
                    item {
                        ShopHeading(R.string.shop_get_multiplayer_games)
                    }
                    items(
                        purchaseStatus.filter { multiplayerGameIds.contains(it.key) && it.value.purchased != true }
                            .map { it.value }
                    ) {
                        ShopItem(product = it, onPurchaseClick)
                    }
                }
//          if (purchaseStatus.any { soloGameIds.contains(it.key) && it.value.purchased != true }) {
//                item {
//                    ShopHeading(R.string.shop_get_solo_games)
//                }
//                items(
//                    purchaseStatus.filter { soloGameIds.contains(it.key) && it.value.purchased != true }
//                        .map { it.value }
//                ) {
//                    ShopItem(product = it, onPurchaseClick)
//                }
//            }
                purchaseStatus[Products.adRemoval]?.let {
                    if (it.purchased != true) {
                        item {
                            ShopHeading(R.string.shop_remove_ads_title)
                        }
                        item {
                            ShopItem(product = it, onPurchaseClick)
                        }
                    }
                }
                purchaseStatus[Products.bundle]?.let {
                    if (it.purchased != true) {
                        item {
                            ShopHeading(R.string.shop_bundle_title)
                        }
                        item {
                            ShopItem(product = it, onPurchaseClick)
                        }
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ShopItemPreview() {
    WelcomeToFlipTheme {
        ShopItem(
            product = InAppProduct(
                WelcomeToTheMoon.mapToProductId(false),
                stringResource(id = WelcomeToTheMoon.displayName),
                "Multiplayer version with the special starship card deck (different distribution to original Welcome To)",
                "1.00",
                "AUD",
                false
            ),
            onPurchaseClick = {}
        )
    }
}

@OptIn(ExperimentalMaterial3AdaptiveApi::class)
@Preview(showBackground = true)
@Preview(showBackground = true, device = Devices.TABLET)
@Preview(showBackground = true, device = "spec:id=reference_tablet,shape=Normal,width=800,height=1280,unit=dp,dpi=240")
@Composable
fun ShopScreenPreview() {
    WelcomeToFlipTheme {
        ShopScreen(
            purchaseStatus = mapOf(
                Pair(
                    Products.adRemoval,
                    InAppProduct(Products.adRemoval, "Ad Removal", "Ad Removal", "1.00", "AUD", false)
                ),
                Pair(Products.bundle, InAppProduct(Products.bundle, "Bundle", "All the games", "10.00", "AUD", false)),
                Pair(
                    WelcomeToTheMoon.mapToProductId(false),
                    InAppProduct(
                        WelcomeToTheMoon.mapToProductId(false),
                        stringResource(id = WelcomeToTheMoon.displayName),
                        "Multiplayer version with the special starship card deck (different distribution to original Welcome To)",
                        "1.00",
                        "AUD",
                        false
                    )
                ),
                Pair(
                    WelcomeToClassic.mapToProductId(true),
                    InAppProduct(
                        WelcomeToClassic.mapToProductId(true),
                        stringResource(id = WelcomeToClassic.displayName),
                        "Solo version with the original construction card deck and set up with AAA validation cards and solo rules.",
                        "1.00",
                        "AUD",
                        false
                    )
                ),
                Pair(
                    WelcomeToTheMoon.mapToProductId(true),
                    InAppProduct(
                        WelcomeToTheMoon.mapToProductId(true),
                        stringResource(id = WelcomeToTheMoon.displayName),
                        "Description",
                        "1.00",
                        "AUD",
                        false
                    )
                )
            ),
            onPurchaseClick = {},
            windowSizeClass = getPreviewWindowSizeClass()
        )
    }
}
