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

@Composable
fun WelcomeToFlipTheme(
        darkTheme: Boolean = false,
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
        colorScheme = WelcomeToClassicColor.lightScheme,
        typography = Typography,
        content = content
    )
}