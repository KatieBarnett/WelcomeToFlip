package dev.veryniche.welcometoflip.core.models

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.ui.text.toLowerCase

data class GameType(
    @DrawableRes val icon: Int,
    @DrawableRes val largeIcon: Int,
    @StringRes val displayName: Int,
    val name: String,
    val purchased: Boolean = false,
    val purchasePrice: String? = null,
    val soloAvailable: Boolean,
    val soloPurchased: Boolean = false,
    val soloPurchasePrice: String? = null,
)

fun String.mapToGameType(): GameType? {
    return when (this.lowercase()) {
        WelcomeToClassic.name.lowercase() -> WelcomeToClassic
        WelcomeToTheMoon.name.lowercase() -> WelcomeToTheMoon
        else -> null
    }
}