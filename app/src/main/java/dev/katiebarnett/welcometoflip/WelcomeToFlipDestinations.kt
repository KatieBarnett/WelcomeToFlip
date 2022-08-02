package dev.katiebarnett.welcometoflip

import androidx.navigation.NavType
import androidx.navigation.navArgument
import androidx.navigation.navDeepLink
import dev.katiebarnett.welcometoflip.core.models.GameType
import dev.katiebarnett.welcometoflip.core.models.SavedGame

sealed class WelcomeToFlipDestination(val route: String)

object ChooseGame : WelcomeToFlipDestination("ChooseGame")

object Game : WelcomeToFlipDestination("Game") {

    const val gameTypeArg = "gameType"
    const val seedArg = "seed"
    const val positionArg = "position"

    val routeWithArgs = "$route/{$gameTypeArg}?$seedArg={$seedArg}&$positionArg={$positionArg}"
    
    val arguments = listOf(
        navArgument(gameTypeArg) { type = NavType.StringType },
        navArgument(seedArg) { type = NavType.StringType; nullable = true },
        navArgument(positionArg) { type = NavType.StringType; nullable = true }
    )
    
    val deepLinks = listOf(
        navDeepLink { uriPattern = "welcomeToFlip://$route/{$gameTypeArg}" },
        navDeepLink { uriPattern = "welcomeToFlip://$route/{$gameTypeArg}?$seedArg=${seedArg}&$positionArg=${positionArg}" }
    )
    
    fun getRoute(gameType: GameType): String {
        return "$route/${gameType.name}"
    }

    fun getRoute(savedGame: SavedGame): String {
        return "$route/${savedGame?.gameType?.name ?: "gameType"}?$seedArg=${savedGame.seed}&$positionArg=${savedGame.position}"
    }
}
