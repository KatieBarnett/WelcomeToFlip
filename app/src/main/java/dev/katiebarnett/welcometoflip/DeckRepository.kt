package dev.katiebarnett.welcometoflip

import dev.katiebarnett.welcometoflip.core.models.*
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DeckRepository @Inject constructor(
) {
    fun getDeck(gameType: GameType): List<Card> {
        return when(gameType) {
            WelcomeToTheMoon -> welcomeToTheMoonDeck
            WelcomeToTheMoonSolo -> welcomeToTheMoonDeck
        }
    }
    
    fun getSoloDeck(gameType: GameType): List<Card> {
        return when(gameType) {
            WelcomeToTheMoon -> listOf()
            WelcomeToTheMoonSolo -> welcomeToTheMoonSoloDeck
        }
    }
}
