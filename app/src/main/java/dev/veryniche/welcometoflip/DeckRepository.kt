package dev.veryniche.welcometoflip

import dev.veryniche.welcometoflip.core.models.Action
import dev.veryniche.welcometoflip.core.models.Card
import dev.veryniche.welcometoflip.core.models.GameType
import dev.veryniche.welcometoflip.core.models.WelcomeToClassic
import dev.veryniche.welcometoflip.core.models.WelcomeToClassicSolo
import dev.veryniche.welcometoflip.core.models.WelcomeToTheMoon
import dev.veryniche.welcometoflip.core.models.WelcomeToTheMoonSolo
import dev.veryniche.welcometoflip.core.models.welcomeToTheMoonAvailableActions
import dev.veryniche.welcometoflip.core.models.welcomeToTheMoonDeck
import dev.veryniche.welcometoflip.core.models.welcomeToTheMoonSoloEffectCards
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DeckRepository @Inject constructor() {
    fun getAvailableGames(): List<GameType> {
        return listOf(WelcomeToClassic, WelcomeToClassicSolo, WelcomeToTheMoon, WelcomeToTheMoonSolo)
    }

    fun getDeck(gameType: GameType): List<Card> {
        return when (gameType) {
            WelcomeToTheMoon, WelcomeToTheMoonSolo -> welcomeToTheMoonDeck
            WelcomeToClassic, WelcomeToClassicSolo -> welcomeToTheMoonDeck // TODO
        }
    }

    fun getSoloEffectCards(gameType: GameType): List<Card> {
        return when (gameType) {
            WelcomeToTheMoon -> listOf()
            WelcomeToTheMoonSolo -> welcomeToTheMoonSoloEffectCards
            WelcomeToClassic -> listOf()
            WelcomeToClassicSolo -> welcomeToTheMoonSoloEffectCards // TODO
        }
    }

    fun getAvailableActions(gameType: GameType): List<Action> {
        return when (gameType) {
            WelcomeToTheMoon, WelcomeToTheMoonSolo -> welcomeToTheMoonAvailableActions
            WelcomeToClassic, WelcomeToClassicSolo -> welcomeToTheMoonAvailableActions // TODO
        }
    }
}
