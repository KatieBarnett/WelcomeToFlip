package dev.katiebarnett.welcometoflip

enum class WelcomeToFlipScreen {
    ChooseGame,
    Game;

    companion object {
        fun fromRoute(route: String?): WelcomeToFlipScreen =
            when (route?.substringBefore("/")) {
                ChooseGame.name -> ChooseGame
                Game.name -> Game
                else -> throw IllegalArgumentException("Route $route is not recognized.")
            }
    }
}