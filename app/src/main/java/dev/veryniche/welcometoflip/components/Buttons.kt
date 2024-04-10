package dev.veryniche.welcometoflip.components

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.rotate
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

@Composable
fun ThemedButton(
    onClick: () -> Unit,
    enabled: Boolean = true,
    modifier: Modifier = Modifier,
    content: @Composable RowScope.() -> Unit
) {
    ElevatedButton(
        colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary),
        onClick = onClick,
        modifier = modifier,
        enabled = enabled,
        content = content
    )
}

@Composable
fun ButtonWithIcon(
    @StringRes textRes: Int,
    @DrawableRes iconRes: Int,
    onClick: () -> Unit,
    enabled: Boolean = true,
    modifier: Modifier = Modifier
) {
    ThemedButton(onClick = onClick, enabled = enabled, modifier) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(Dimen.Button.iconSpacing)
        ) {
            Icon(
                painter = painterResource(id = iconRes),
                contentDescription = stringResource(id = textRes),
                modifier = Modifier
                    .size(Dimen.Button.iconSize)
            )
            Text(stringResource(id = textRes))
        }
    }
}

@Composable
fun ThemedIconButton(
    @StringRes altTextRes: Int? = null,
    @DrawableRes iconRes: Int,
    onClick: () -> Unit,
    enabled: Boolean = true,
    modifier: Modifier = Modifier
) {
    ElevatedButton(
        colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary),
        onClick = onClick,
        modifier = modifier.size(Dimen.Button.iconButtonSize),
        contentPadding = PaddingValues(Dimen.Button.iconButtonContentPadding),
        enabled = enabled
    ) {
        Icon(
            painter = painterResource(id = iconRes),
            contentDescription = altTextRes ?.let {
                stringResource(id = altTextRes)
            },
            modifier = Modifier.size(Dimen.Button.iconButtonIconSize)
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GameButton(
    @StringRes textRes: Int,
    @DrawableRes imageRes: Int,
    purchased: Boolean,
    solo: Boolean,
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
        onClick = onClick,
        modifier = modifier.aspectRatio(
            if (purchased) {
                3 / 1.8f
            } else {
                3 / 1f
            }
        )
    ) {
        Box(modifier = Modifier) {
            Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier) {
                Text(
                    stringResource(textRes),
                    style = if (purchased) {
                        MaterialTheme.typography.displayMedium
                    } else {
                        MaterialTheme.typography.displaySmall
                    },
                    color = MaterialTheme.colorScheme.onPrimary,
                    modifier = Modifier
                        .weight(1f)
                        .padding(Dimen.spacingDouble)
                )
                Box(
                    Modifier.weight(
                        if (purchased) {
                            0.66f
                        } else {
                            0.2f
                        }
                    )
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
            if (solo) {
                Text(
                    text = stringResource(id = dev.veryniche.welcometoflip.R.string.game_solo),
                    textAlign = TextAlign.Center,
                    color = MaterialTheme.colorScheme.onTertiaryContainer,
                    modifier = Modifier
                        .align(Alignment.CenterEnd)
                        .rotate(-45f)
                        .fillMaxWidth()
                        .padding(Dimen.spacing)
                        .background(MaterialTheme.colorScheme.tertiaryContainer)
                )
            }
        }
    }
}

@Preview(group = "Buttons", showBackground = true)
@Composable
fun ThemedButtonPreview() {
    WelcomeToFlipTheme {
        ThemedButton(content = {
            Text("Button Text")
        }, onClick = {})
    }
}

@Preview(group = "Buttons", showBackground = true)
@Composable
fun ButtonWithIconPreview() {
    WelcomeToFlipTheme {
        ButtonWithIcon(
            textRes = R.string.game_welcome_to_the_moon,
            R.drawable.noun_rocket_4925595,
            onClick = {}
        )
    }
}

@Preview(group = "Buttons", showBackground = true)
@Composable
fun IconButtonPreview() {
    WelcomeToFlipTheme {
        ThemedIconButton(
            altTextRes = R.string.game_welcome_to_the_moon,
            R.drawable.noun_bin_2034046,
            onClick = {}
        )
    }
}

@Preview(group = "Game Buttons", showBackground = true)
@Composable
fun GameButtonPurchasedPreview() {
    WelcomeToFlipTheme {
        Box(Modifier.padding(Dimen.spacingDouble)) {
            GameButton(
                textRes = R.string.game_welcome_to_the_moon,
                R.drawable.noun_moon_6086589,
                purchased = true,
                solo = false,
                onClick = {}
            )
        }
    }
}

@Preview(group = "Game Buttons", showBackground = true)
@Composable
fun GameButtonSoloPurchasedPreview() {
    WelcomeToFlipTheme {
        Box(Modifier.padding(Dimen.spacingDouble)) {
            GameButton(
                textRes = R.string.game_welcome_to_the_moon,
                R.drawable.noun_moon_6086589,
                purchased = true,
                solo = true,
                onClick = {}
            )
        }
    }
}

@Preview(group = "Game Buttons", showBackground = true)
@Composable
fun GameButtonPreview() {
    WelcomeToFlipTheme {
        Box(Modifier.padding(Dimen.spacingDouble)) {
            GameButton(
                textRes = R.string.game_welcome_to_the_moon,
                R.drawable.noun_moon_6086589,
                purchased = false,
                solo = false,
                onClick = {}
            )
        }
    }
}

@Preview(group = "Game Buttons", showBackground = true)
@Composable
fun GameButtonSoloPreview() {
    WelcomeToFlipTheme {
        Box(Modifier.padding(Dimen.spacingDouble)) {
            GameButton(
                textRes = R.string.game_welcome_to_the_moon,
                R.drawable.noun_moon_6086589,
                purchased = false,
                solo = true,
                onClick = {}
            )
        }
    }
}
