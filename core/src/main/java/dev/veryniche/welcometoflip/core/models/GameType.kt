package dev.veryniche.welcometoflip.core.models

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes

data class GameType(
    @DrawableRes val icon: Int,
    @DrawableRes val largeIcon: Int,
    @StringRes val displayName: Int,
    val name: String,
    val purchased: Boolean? = null,
    val purchasePrice: String? = null,
    val solo: Boolean,
    val soloPurchased: Boolean? = null,
    val soloPurchasePrice: String? = null,
)

fun String.mapToGameType(): GameType? {
    return when (this) {
        WelcomeToClassic.name -> WelcomeToClassic
        WelcomeToTheMoon.name -> WelcomeToTheMoon
        else -> null
    }
}
