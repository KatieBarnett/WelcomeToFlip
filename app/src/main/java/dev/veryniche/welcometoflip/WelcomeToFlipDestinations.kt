package dev.veryniche.welcometoflip

import androidx.navigation.NavType
import androidx.navigation.navArgument
import androidx.navigation.navDeepLink
import dev.veryniche.welcometoflip.core.models.GameType
import dev.veryniche.welcometoflip.core.models.SavedGame

sealed class WelcomeToFlipDestination(val route: String)

data object ChooseGame : WelcomeToFlipDestination("ChooseGame")
data object About : WelcomeToFlipDestination("About")
data object Shop : WelcomeToFlipDestination("Shop")

data object ChartScreen : WelcomeToFlipDestination("ChartScreen") {

    const val gameTypeArg = "gameType"
    val routeWithArgs = "$route/{$gameTypeArg}"
    val arguments = listOf(
        navArgument(gameTypeArg) { type = NavType.StringType },
    )

    val deepLinks = listOf(
        navDeepLink { uriPattern = "welcomeToFlip://$route/{$gameTypeArg}" },
        navDeepLink {
            uriPattern = "welcomeToFlip://$route/{$gameTypeArg}"
        }
    )

    fun getRoute(gameType: GameType): String {
        return "$route/${gameType.name}"
    }
}

data object Game : WelcomeToFlipDestination("Game") {

    const val gameTypeArg = "gameType"
    const val seedArg = "seed"
    const val positionArg = "position"

    val routeWithArgs = "$route/{$gameTypeArg}?$seedArg={$seedArg}&$positionArg={$positionArg}"

    val arguments = listOf(
        navArgument(gameTypeArg) { type = NavType.StringType },
        navArgument(seedArg) {
            type = NavType.StringType
            nullable = true
        },
        navArgument(positionArg) {
            type = NavType.StringType
            nullable = true
        }
    )

    val deepLinks = listOf(
        navDeepLink { uriPattern = "welcomeToFlip://$route/{$gameTypeArg}" },
        navDeepLink {
            uriPattern = "welcomeToFlip://$route/{$gameTypeArg}" +
                "?$seedArg=$seedArg&$positionArg=$positionArg"
        }
    )

    fun getRoute(gameType: GameType): String {
        return "$route/${gameType.name}"
    }

    fun getRoute(savedGame: SavedGame): String {
        return "$route/${savedGame.gameType?.name ?: "gameType"}" +
            "?$seedArg=${savedGame.seed}&$positionArg=${savedGame.position}"
    }
}

data object SoloGame : WelcomeToFlipDestination("SoloGame") {

    const val gameTypeArg = "gameType"
    const val seedArg = "seed"

    // Add in state

    val routeWithArgs = "$route/{$gameTypeArg}?$seedArg={$seedArg}"

    val arguments = listOf(
        navArgument(gameTypeArg) { type = NavType.StringType },
        navArgument(seedArg) {
            type = NavType.StringType
            nullable = true
        }
    )

    val deepLinks = listOf(
        navDeepLink { uriPattern = "welcomeToFlip://$route/{$gameTypeArg}" },
        navDeepLink {
            uriPattern = "welcomeToFlip://$route/{$gameTypeArg}" +
                "?$seedArg=$seedArg"
        }
    )

    fun getRoute(gameType: GameType): String {
        return "$route/${gameType.name}"
    }
//
//    fun getRoute(savedGame: SavedGame): String {
//        return "$route/${savedGame.gameType?.name ?: "gameType"}" +
//            "?$soloArg=${savedGame.solo}&$seedArg=${savedGame.seed}&$positionArg=${savedGame.position}"
//    }
}
