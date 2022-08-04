package dev.katiebarnett.welcometoflip.core.models

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes

sealed class GameType(
    @DrawableRes open val icon: Int,
    @StringRes open val displayName: Int,
    open val solo: Boolean,
    open val name: String
)

fun String.mapToGameType() : GameType? {
    return when(this) {
        WelcomeToTheMoon.name -> WelcomeToTheMoon
        else -> null
    }
}
