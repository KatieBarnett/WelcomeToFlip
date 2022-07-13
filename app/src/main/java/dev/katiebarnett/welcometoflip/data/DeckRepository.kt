package dev.katiebarnett.welcometoflip.data

import dev.katiebarnett.welcometoflip.models.Card
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.IOException
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DeckRepository @Inject constructor(
) {
    
    @Suppress("BlockingMethodInNonBlockingContext")
    @Throws(IOException::class)
    suspend fun getDeck(gameType: GameType): List<Card> {
        return withContext(Dispatchers.IO) {
            when(gameType) {
                WelcomeToTheMoon -> welcomeToTheMoonDeck
            }
        }
    }
}
