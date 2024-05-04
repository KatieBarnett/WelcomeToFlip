package dev.veryniche.welcometoflip.components

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.size
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
