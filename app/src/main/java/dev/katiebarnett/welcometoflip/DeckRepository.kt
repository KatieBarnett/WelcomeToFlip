package dev.katiebarnett.welcometoflip

import dev.katiebarnett.welcometoflip.core.models.*
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DeckRepository @Inject constructor(
) {
    fun getAvailableGames(): List<GameType> {
        return listOf(WelcomeToTheMoon)
//        return listOf(WelcomeToTheMoon, WelcomeToTheMoonSolo)
    }
    
    fun getDeck(gameType: GameType): List<Card> {
        return when(gameType) {
            WelcomeToTheMoon, WelcomeToTheMoonSolo -> welcomeToTheMoonDeck
        }
    }
    
    fun getSoloEffectCards(gameType: GameType): List<Card> {
        return when(gameType) {
            WelcomeToTheMoon -> listOf()
            WelcomeToTheMoonSolo -> welcomeToTheMoonSoloEffectCards
        }
    }
    
    fun getAvailableActions(gameType: GameType): List<Action> {
        return when(gameType) {
            WelcomeToTheMoon, WelcomeToTheMoonSolo -> welcomeToTheMoonAvailableActions
        }
    }
}
