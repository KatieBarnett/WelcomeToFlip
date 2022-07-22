package dev.katiebarnett.welcometoflip.data

import dev.katiebarnett.welcometoflip.models.Card
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DeckRepository @Inject constructor(
) {
    fun getDeck(gameType: GameType): List<Card> {
        return when(gameType) {
            WelcomeToTheMoon -> welcomeToTheMoonDeck
        }
    }
}
