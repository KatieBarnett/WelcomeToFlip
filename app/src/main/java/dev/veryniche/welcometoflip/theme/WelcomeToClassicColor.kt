package dev.veryniche.welcometoflip.theme

import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.ui.graphics.Color

object WelcomeToClassicColor {

    private val primaryLight = Color(0xFF356851)
    private val onPrimaryLight = Color(0xFFFFFFFF)
    private val primaryContainerLight = Color(0xFF99CEB3)
    private val onPrimaryContainerLight = Color(0xFF003B28)
    private val secondaryLight = Color(0xFF1E4F33)
    private val onSecondaryLight = Color(0xFFFFFFFF)
    private val secondaryContainerLight = Color(0xFF437455)
    private val onSecondaryContainerLight = Color(0xFFFFFFFF)
    private val tertiaryLight = Color(0xFFA50005)
    private val onTertiaryLight = Color(0xFFFFFFFF)
    private val tertiaryContainerLight = Color(0xFFE1291F)
    private val onTertiaryContainerLight = Color(0xFFFFFFFF)
    private val errorLight = Color(0xFFBA1A1A)
    private val onErrorLight = Color(0xFFFFFFFF)
    private val errorContainerLight = Color(0xFFFFDAD6)
    private val onErrorContainerLight = Color(0xFF410002)
    private val backgroundLight = Color(0xFFF8FAF6)
    private val onBackgroundLight = Color(0xFF191C1A)
    private val surfaceLight = Color(0xFFF8FAF6)
    private val onSurfaceLight = Color(0xFF191C1A)
    private val surfaceVariantLight = Color(0xFFDCE5DD)
    private val onSurfaceVariantLight = Color(0xFF404944)
    private val outlineLight = Color(0xFF717973)
    private val outlineVariantLight = Color(0xFFC0C9C2)
    private val scrimLight = Color(0xFF000000)
    private val inverseSurfaceLight = Color(0xFF2E312F)
    private val inverseOnSurfaceLight = Color(0xFFF0F1ED)
    private val inversePrimaryLight = Color(0xFF9DD2B7)
    private val surfaceDimLight = Color(0xFFD9DAD7)
    private val surfaceBrightLight = Color(0xFFF8FAF6)
    private val surfaceContainerLowestLight = Color(0xFFFFFFFF)
    private val surfaceContainerLowLight = Color(0xFFF3F4F0)
    private val surfaceContainerLight = Color(0xFFEDEEEB)
    private val surfaceContainerHighLight = Color(0xFFE7E9E5)
    private val surfaceContainerHighestLight = Color(0xFFE1E3DF)

    private val primaryDark = Color(0xFFB2E8CC)
    private val onPrimaryDark = Color(0xFF003826)
    private val primaryContainerDark = Color(0xFF89BEA4)
    private val onPrimaryContainerDark = Color(0xFF002E1E)
    private val secondaryDark = Color(0xFF9ED3AD)
    private val onSecondaryDark = Color(0xFF01391F)
    private val secondaryContainerDark = Color(0xFF28593C)
    private val onSecondaryContainerDark = Color(0xFFC6FCD5)
    private val tertiaryDark = Color(0xFFFFB4A9)
    private val onTertiaryDark = Color(0xFF690002)
    private val tertiaryContainerDark = Color(0xFFDE261E)
    private val onTertiaryContainerDark = Color(0xFFFFFFFF)
    private val errorDark = Color(0xFFFFB4AB)
    private val onErrorDark = Color(0xFF690005)
    private val errorContainerDark = Color(0xFF93000A)
    private val onErrorContainerDark = Color(0xFFFFDAD6)
    private val backgroundDark = Color(0xFF111412)
    private val onBackgroundDark = Color(0xFFE1E3DF)
    private val surfaceDark = Color(0xFF111412)
    private val onSurfaceDark = Color(0xFFE1E3DF)
    private val surfaceVariantDark = Color(0xFF404944)
    private val onSurfaceVariantDark = Color(0xFFC0C9C2)
    private val outlineDark = Color(0xFF8A938D)
    private val outlineVariantDark = Color(0xFF404944)
    private val scrimDark = Color(0xFF000000)
    private val inverseSurfaceDark = Color(0xFFE1E3DF)
    private val inverseOnSurfaceDark = Color(0xFF2E312F)
    private val inversePrimaryDark = Color(0xFF356851)
    private val surfaceDimDark = Color(0xFF111412)
    private val surfaceBrightDark = Color(0xFF373A38)
    private val surfaceContainerLowestDark = Color(0xFF0C0F0D)
    private val surfaceContainerLowDark = Color(0xFF191C1A)
    private val surfaceContainerDark = Color(0xFF1D201E)
    private val surfaceContainerHighDark = Color(0xFF282B29)
    private val surfaceContainerHighestDark = Color(0xFF333533)

    private val backgroundLightOverride = Color(0xFFFAF1DA)

    val lightScheme = lightColorScheme(
        primary = primaryLight,
        onPrimary = onPrimaryLight,
        primaryContainer = primaryContainerLight,
        onPrimaryContainer = onPrimaryContainerLight,
        secondary = secondaryLight,
        onSecondary = onSecondaryLight,
        secondaryContainer = secondaryContainerLight,
        onSecondaryContainer = onSecondaryContainerLight,
        tertiary = tertiaryLight,
        onTertiary = onTertiaryLight,
        tertiaryContainer = tertiaryContainerLight,
        onTertiaryContainer = onTertiaryContainerLight,
        error = errorLight,
        onError = onErrorLight,
        errorContainer = errorContainerLight,
        onErrorContainer = onErrorContainerLight,
        background = backgroundLightOverride, //backgroundLight,
        onBackground = onBackgroundLight,
        surface = surfaceLight,
        onSurface = onSurfaceLight,
        surfaceVariant = surfaceVariantLight,
        onSurfaceVariant = onSurfaceVariantLight,
        outline = outlineLight,
        outlineVariant = outlineVariantLight,
        scrim = scrimLight,
        inverseSurface = inverseSurfaceLight,
        inverseOnSurface = inverseOnSurfaceLight,
        inversePrimary = inversePrimaryLight,
        surfaceDim = surfaceDimLight,
        surfaceBright = surfaceBrightLight,
        surfaceContainerLowest = surfaceContainerLowestLight,
        surfaceContainerLow = surfaceContainerLowLight,
        surfaceContainer = surfaceContainerLight,
        surfaceContainerHigh = surfaceContainerHighLight,
        surfaceContainerHighest = surfaceContainerHighestLight,
    )

    val darkScheme = darkColorScheme(
        primary = primaryDark,
        onPrimary = onPrimaryDark,
        primaryContainer = primaryContainerDark,
        onPrimaryContainer = onPrimaryContainerDark,
        secondary = secondaryDark,
        onSecondary = onSecondaryDark,
        secondaryContainer = secondaryContainerDark,
        onSecondaryContainer = onSecondaryContainerDark,
        tertiary = tertiaryDark,
        onTertiary = onTertiaryDark,
        tertiaryContainer = tertiaryContainerDark,
        onTertiaryContainer = onTertiaryContainerDark,
        error = errorDark,
        onError = onErrorDark,
        errorContainer = errorContainerDark,
        onErrorContainer = onErrorContainerDark,
        background = backgroundDark,
        onBackground = onBackgroundDark,
        surface = surfaceDark,
        onSurface = onSurfaceDark,
        surfaceVariant = surfaceVariantDark,
        onSurfaceVariant = onSurfaceVariantDark,
        outline = outlineDark,
        outlineVariant = outlineVariantDark,
        scrim = scrimDark,
        inverseSurface = inverseSurfaceDark,
        inverseOnSurface = inverseOnSurfaceDark,
        inversePrimary = inversePrimaryDark,
        surfaceDim = surfaceDimDark,
        surfaceBright = surfaceBrightDark,
        surfaceContainerLowest = surfaceContainerLowestDark,
        surfaceContainerLow = surfaceContainerLowDark,
        surfaceContainer = surfaceContainerDark,
        surfaceContainerHigh = surfaceContainerHighDark,
        surfaceContainerHighest = surfaceContainerHighestDark,
    )
    
}