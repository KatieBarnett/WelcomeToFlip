package dev.veryniche.welcometoflip.components

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.twotone.Groups
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import dev.veryniche.welcometoflip.core.R
import dev.veryniche.welcometoflip.theme.Dimen
import dev.veryniche.welcometoflip.theme.WelcomeToFlipTheme

const val CARD_ASPECT_RATIO = 3 / 1.8f

@Composable
fun GameTile(
    @StringRes textRes: Int,
    @DrawableRes imageRes: Int,
    purchased: Boolean,
    purchasePrice: String?,
    solo: Boolean,
    soloPurchased: Boolean,
    soloPurchasePrice: String?,
    onClick: (solo: Boolean) -> Unit,
    modifier: Modifier = Modifier
) {
    Tile(
        sideFront = { modifier ->
            GameTileFront(
                textRes = textRes,
                imageRes = imageRes,
                anyPurchased = purchased || soloPurchased,
                soloAvailable = solo,
                modifier = modifier
            )
        },
        sideBack = { modifier ->
            GameTileBack(
                textRes = textRes,
                imageRes = imageRes,
                purchased = purchased,
                purchasePrice = purchasePrice,
                solo = solo,
                soloPurchased = soloPurchased,
                soloPurchasePrice = soloPurchasePrice,
                onClick = {
                    onClick.invoke(false)
                },
                onClickSolo = {
                    onClick.invoke(true)
                },
                modifier = modifier
            )
        },
        modifier = modifier.aspectRatio(CARD_ASPECT_RATIO)
    )
}

@Composable
fun GameTileFront(
    @StringRes textRes: Int,
    @DrawableRes imageRes: Int,
    anyPurchased: Boolean,
    soloAvailable: Boolean,
    modifier: Modifier = Modifier
) {
    ElevatedCard(
        elevation = CardDefaults.cardElevation(
            defaultElevation = 10.dp
        ),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primary,
        ),
        modifier = modifier.fillMaxSize()
    ) {
        Box(modifier = Modifier) {
            Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier) {
                Text(
                    stringResource(textRes),
                    style = MaterialTheme.typography.displayMedium,
                    color = MaterialTheme.colorScheme.onPrimary,
                    modifier = Modifier
                        .weight(1f)
                        .padding(Dimen.spacingDouble)
                )
                Box(
                    Modifier.weight(0.66f)
                ) {
                    Image(
                        painterResource(imageRes),
                        contentDescription = stringResource(textRes),
                        colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.onPrimary),
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .alpha(0.4f)
                            .fillMaxHeight()
                            .wrapContentWidth(Alignment.Start, unbounded = true)
                            .aspectRatio(1f)
                    )
                }
            }
            Row(
                modifier = Modifier.align(Alignment.BottomEnd),
                verticalAlignment = Alignment.Bottom
            ) {
                Image(
                    imageVector = Icons.TwoTone.Groups,
                    colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.onPrimary),
                    contentDescription = "",
                    contentScale = ContentScale.FillWidth,
                    modifier = Modifier.width(48.dp).padding(end = 6.dp)
                )
                if (soloAvailable) {
                    Image(
                        imageVector = Icons.Filled.Person,
                        colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.onPrimary),
                        contentDescription = "",
                        contentScale = ContentScale.FillWidth,
                        modifier = Modifier.padding(bottom = 4.dp, end = 6.dp).width(32.dp)
                    )
                }
            }
        }
    }
}

@Composable
fun GameTileBack(
    @StringRes textRes: Int,
    @DrawableRes imageRes: Int,
    purchased: Boolean,
    purchasePrice: String?,
    solo: Boolean,
    soloPurchased: Boolean,
    soloPurchasePrice: String?,
    onClickSolo: () -> Unit,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    ElevatedCard(
        elevation = CardDefaults.cardElevation(
            defaultElevation = 10.dp
        ),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primary,
        ),
        modifier = modifier.fillMaxSize()
    ) {
        Text(
            text = stringResource(textRes),
            style = MaterialTheme.typography.headlineSmall,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .padding(Dimen.spacingDouble)
                .fillMaxWidth()
        )
        Column(modifier = Modifier.padding(16.dp).weight(1f), verticalArrangement = Arrangement.SpaceAround) {
            ElevatedButton(
                onClick = {
                    onClick.invoke()
                },
                Modifier.fillMaxWidth()
            ) {
                Text(getButtonText(purchased = purchased, solo = false, purchasePrice = purchasePrice))
            }
            if (solo) {
                ElevatedButton(
                    onClick = {
                        onClickSolo.invoke()
                    },
                    Modifier.fillMaxWidth()
                ) {
                    Text(getButtonText(purchased = soloPurchased, solo = true, purchasePrice = soloPurchasePrice))
                }
            }
        }
    }
}

@Composable
fun getButtonText(
    purchased: Boolean,
    solo: Boolean,
    purchasePrice: String?
) = if (purchasePrice == null && !purchased) {
    if (solo) {
        stringResource(id = dev.veryniche.welcometoflip.R.string.solo_not_purchased_no_price)
    } else {
        stringResource(id = dev.veryniche.welcometoflip.R.string.multiplayer_not_purchased_no_price)
    }
} else if (purchased || purchasePrice == null) {
    if (solo) {
        stringResource(id = dev.veryniche.welcometoflip.R.string.solo_purchased)
    } else {
        stringResource(id = dev.veryniche.welcometoflip.R.string.multiplayer_purchased)
    }
} else {
    if (solo) {
        stringResource(id = dev.veryniche.welcometoflip.R.string.solo_not_purchased, purchasePrice)
    } else {
        stringResource(id = dev.veryniche.welcometoflip.R.string.multiplayer_not_purchased, purchasePrice)
    }
}

@Preview(showBackground = true)
@Composable
fun GameTilePurchasedPreview() {
    WelcomeToFlipTheme {
        Box(Modifier.padding(Dimen.spacingDouble)) {
            GameTile(
                textRes = R.string.game_welcome_to_the_moon,
                R.drawable.noun_moon_6086589,
                purchased = true,
                purchasePrice = "5.00",
                solo = false,
                soloPurchasePrice = "1.00",
                soloPurchased = false,
                onClick = {}
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun GameTileSoloPurchasedPreview() {
    WelcomeToFlipTheme {
        Box(Modifier.padding(Dimen.spacingDouble)) {
            GameTile(
                textRes = R.string.game_welcome_to_the_moon,
                R.drawable.noun_moon_6086589,
                purchased = true,
                purchasePrice = "5.00",
                solo = true,
                soloPurchasePrice = "1.00",
                soloPurchased = true,
                onClick = {}
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun GameTileNotPurchasedPreview() {
    WelcomeToFlipTheme {
        Box(Modifier.padding(Dimen.spacingDouble)) {
            GameTile(
                textRes = R.string.game_welcome_to_the_moon,
                R.drawable.noun_moon_6086589,
                purchased = false,
                purchasePrice = "5.00",
                solo = false,
                soloPurchasePrice = "1.00",
                soloPurchased = false,
                onClick = {}
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun GameTileBackPurchasedPreview() {
    WelcomeToFlipTheme {
        Box(Modifier.padding(Dimen.spacingDouble)) {
            GameTileBack(
                textRes = R.string.game_welcome_to_the_moon,
                R.drawable.noun_moon_6086589,
                purchased = true,
                purchasePrice = "5.00",
                solo = false,
                soloPurchasePrice = "1.00",
                soloPurchased = false,
                onClickSolo = {},
                onClick = {},
                modifier = Modifier.aspectRatio(CARD_ASPECT_RATIO)
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun GameTileBackSoloPurchasedPreview() {
    WelcomeToFlipTheme {
        Box(Modifier.padding(Dimen.spacingDouble)) {
            GameTileBack(
                textRes = R.string.game_welcome_to_the_moon,
                R.drawable.noun_moon_6086589,
                purchased = true,
                purchasePrice = "5.00",
                solo = true,
                soloPurchasePrice = "1.00",
                soloPurchased = true,
                onClickSolo = {},
                onClick = {},
                modifier = Modifier.aspectRatio(CARD_ASPECT_RATIO)
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun GameTileBackNotPurchasedPreview() {
    WelcomeToFlipTheme {
        Box(Modifier.padding(Dimen.spacingDouble)) {
            GameTileBack(
                textRes = R.string.game_welcome_to_the_moon,
                R.drawable.noun_moon_6086589,
                purchased = false,
                purchasePrice = "5.00",
                solo = false,
                soloPurchasePrice = "1.00",
                soloPurchased = false,
                onClickSolo = {},
                onClick = {},
                modifier = Modifier.aspectRatio(CARD_ASPECT_RATIO)
            )
        }
    }
}
