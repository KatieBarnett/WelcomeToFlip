package dev.katiebarnett.welcometoflip.components

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import dev.katiebarnett.welcometoflip.R
import dev.katiebarnett.welcometoflip.theme.Dimen
import dev.katiebarnett.welcometoflip.theme.WelcomeToFlipTheme

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
        enabled= enabled,
        content = content
    )
}

@Composable
fun ButtonWithIcon(@StringRes textRes: Int,
                   @DrawableRes iconRes: Int,
                   onClick: () -> Unit,
                   enabled: Boolean = true,
                   modifier: Modifier = Modifier) {
    ThemedButton(onClick = onClick, enabled = enabled, modifier) {
        Row(verticalAlignment = Alignment.CenterVertically, 
            horizontalArrangement = Arrangement.spacedBy(Dimen.Button.iconSpacing)) {
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

@Preview(showBackground = true)
@Composable
fun ThemedButtonPreview() {
    WelcomeToFlipTheme {
        ThemedButton(content = {
            Text("Button Text")
        }, onClick = {})
    }
}

@Preview(showBackground = true)
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
