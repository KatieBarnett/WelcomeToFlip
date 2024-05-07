package dev.veryniche.welcometoflip.previews

import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
@Composable
internal fun getPreviewWindowSizeClass(): WindowSizeClass {
    val configuration = LocalConfiguration.current
    val size = DpSize(
        width = configuration.screenWidthDp.dp,
        height = configuration.screenHeightDp.dp,
    )
    return WindowSizeClass.calculateFromSize(size)
}
