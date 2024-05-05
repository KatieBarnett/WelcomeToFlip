package dev.veryniche.welcometoflip.components

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.size
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import dev.veryniche.welcometoflip.core.R
import dev.veryniche.welcometoflip.theme.Dimen
import dev.veryniche.welcometoflip.theme.WelcomeToFlipTheme

@Composable
fun ThemedButton(
    onClick: () -> Unit,
    colors: ButtonColors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary),
    enabled: Boolean = true,
    modifier: Modifier = Modifier,
    content: @Composable RowScope.() -> Unit
) {
    ElevatedButton(
        colors = colors,
        onClick = onClick,
        modifier = modifier,
        enabled = enabled,
        content = content
    )
}

@Composable
fun ThemedButtonWithIcon(
    @StringRes textRes: Int?,
    @StringRes imageAltTextRes: Int? = null,
    @DrawableRes iconRes: Int,
    colors: ButtonColors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary),
    onClick: () -> Unit,
    enabled: Boolean = true,
    modifier: Modifier = Modifier
) {
    ThemedButton(onClick = onClick, enabled = enabled, colors = colors, modifier = modifier) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(Dimen.Button.iconSpacing)
        ) {
            Icon(
                painter = painterResource(id = iconRes),
                contentDescription = stringResource(id = textRes ?: imageAltTextRes ?: 0),
                modifier = Modifier
                    .size(Dimen.Button.iconSize)
            )
            textRes?.let {
                Text(stringResource(id = textRes))
            }
        }
    }
}

@Composable
fun ThemedIconButton(
    @StringRes altTextRes: Int? = null,
    @DrawableRes iconRes: Int,
    colors: ButtonColors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary),
    onClick: () -> Unit,
    enabled: Boolean = true,
    modifier: Modifier = Modifier
) {
    ElevatedButton(
        colors = colors,
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
fun ThemedButtonWithIconPreview() {
    WelcomeToFlipTheme {
        ThemedButtonWithIcon(
            textRes = R.string.game_welcome_to_the_moon,
            iconRes = R.drawable.noun_rocket_4925595,
            onClick = {}
        )
    }
}

@Preview(group = "Buttons", showBackground = true)
@Composable
fun ThemedButtonWithIconPreviewJustIcon() {
    WelcomeToFlipTheme {
        ThemedButtonWithIcon(
            imageAltTextRes = R.string.game_welcome_to_the_moon,
            iconRes = R.drawable.noun_rocket_4925595,
            textRes = null,
            onClick = {}
        )
    }
}

@Preview(group = "Buttons", showBackground = true)
@Composable
fun ThemedIconButtonPreview() {
    WelcomeToFlipTheme {
        ThemedIconButton(
            altTextRes = R.string.game_welcome_to_the_moon,
            iconRes = R.drawable.noun_bin_2034046,
            onClick = {}
        )
    }
}
