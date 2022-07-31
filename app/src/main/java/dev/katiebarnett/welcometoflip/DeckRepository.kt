package dev.katiebarnett.welcometoflip

import dev.katiebarnett.welcometoflip.core.models.Card
import dev.katiebarnett.welcometoflip.core.models.GameType
import dev.katiebarnett.welcometoflip.core.models.WelcomeToTheMoon
import dev.katiebarnett.welcometoflip.core.models.welcomeToTheMoonDeck
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
