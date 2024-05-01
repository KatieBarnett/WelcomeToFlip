package dev.veryniche.welcometoflip

import dev.veryniche.welcometoflip.core.models.Action
import dev.veryniche.welcometoflip.core.models.Card
import dev.veryniche.welcometoflip.core.models.GameType
import dev.veryniche.welcometoflip.core.models.WelcomeToClassic
import dev.veryniche.welcometoflip.core.models.WelcomeToTheMoon
import dev.veryniche.welcometoflip.core.models.welcomeToClassicDeck
import dev.veryniche.welcometoflip.core.models.welcomeToClassicSoloEffectCards
import dev.veryniche.welcometoflip.core.models.welcomeToTheMoonAvailableActions
import dev.veryniche.welcometoflip.core.models.welcomeToTheMoonDeck
import dev.veryniche.welcometoflip.core.models.welcomeToTheMoonSoloEffectCards
import timber.log.Timber
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DeckRepository @Inject constructor() {

    fun getAvailableGames(): List<GameType> {
        return listOf(WelcomeToClassic, WelcomeToTheMoon)
    }

    fun getDeck(gameType: GameType): List<Card> {
        return when (gameType) {
            WelcomeToTheMoon -> welcomeToTheMoonDeck
            WelcomeToClassic -> welcomeToClassicDeck
            else -> {
                Timber.e("Unknown game type $gameType")
                throw Exception("Unknown game type")
            }
        }
    }

    fun getSoloEffectCards(gameType: GameType): List<Card> {
        return when (gameType) {
            WelcomeToTheMoon -> welcomeToTheMoonSoloEffectCards
            WelcomeToClassic -> welcomeToClassicSoloEffectCards
            else -> {
                Timber.e("Unknown game type $gameType")
                throw Exception("Unknown game type")
            }
        }
    }

    fun getAvailableActions(gameType: GameType): List<Action> {
        return when (gameType) {
            WelcomeToTheMoon -> welcomeToTheMoonAvailableActions
            WelcomeToClassic -> welcomeToTheMoonAvailableActions // TODO
            else -> {
                Timber.e("Unknown game type $gameType")
                throw Exception("Unknown game type")
            }
        }
    }
}
