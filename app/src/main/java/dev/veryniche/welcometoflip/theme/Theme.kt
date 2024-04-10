package dev.veryniche.welcometoflip.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import dev.veryniche.welcometoflip.core.theme.Black
import dev.veryniche.welcometoflip.core.theme.Cream
import dev.veryniche.welcometoflip.core.theme.GreyLight
import dev.veryniche.welcometoflip.core.theme.Plum
import dev.veryniche.welcometoflip.core.theme.White

// TODO: Night mode
private val DarkColorScheme = darkColorScheme(
    primary = Plum,
    secondary = Black,
    background = White,
    surface = GreyLight,
    onPrimary = Cream,
    onSecondary = White,
    onBackground = Black,
    onSurface = Black,
    outline = Plum
)

private val LightColorScheme = lightColorScheme(
    primary = Plum,
    secondary = Black,
    background = White,
    surface = GreyLight,
    onPrimary = Cream,
    onSecondary = White,
    onBackground = Black,
    onSurface = Black,
    outline = Plum
)

@Composable
fun WelcomeToFlipTheme(
        darkTheme: Boolean = false,
        // Dynamic color is available on Android 12+
//        dynamicColor: Boolean = false,
        content: @Composable () -> Unit
) {
//    val colorScheme = when {
//        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
//            val context = LocalContext.current
//            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
//        }
//        darkTheme -> DarkColorScheme
//        else -> LightColorScheme
//    }
//    val view = LocalView.current
//    if (!view.isInEditMode) {
//        SideEffect {
//            (view.context as Activity?)?.window?.statusBarColor = colorScheme.primary.toArgb()
//            ViewCompat.getWindowInsetsController(view)?.isAppearanceLightStatusBars = darkTheme
//        }
//    }

    MaterialTheme(
        colorScheme = LightColorScheme,
        typography = Typography,
        content = content
    )
}