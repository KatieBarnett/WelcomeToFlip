package dev.katiebarnett.welcometoflip

import androidx.navigation.NavType
import androidx.navigation.navArgument
import androidx.navigation.navDeepLink
import dev.katiebarnett.welcometoflip.core.models.GameType

sealed class WelcomeToFlipDestination(val route: String)

object ChooseGame : WelcomeToFlipDestination("ChooseGame")

object Game : WelcomeToFlipDestination("Game") {

    const val gameTypeArg = "gameType"

    val routeWithArgs = "$route/{$gameTypeArg}"
    val arguments = listOf(
        navArgument(gameTypeArg) { type = NavType.StringType }
    )
    val deepLinks = listOf(
        navDeepLink { uriPattern = "welcomeToFlip://$route/{$gameTypeArg}" }
    )
    fun getRoute(gameType: GameType): String {
        return "$route/${gameType.name}"
    }
}