package dev.veryniche.welcometoflip.storage

import dev.veryniche.welcometoflip.core.models.GameType
import dev.veryniche.welcometoflip.core.models.SavedGame
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SavedGamesRepository @Inject constructor(
    private val savedGamesDataSource: SavedGamesDataSource
) {

    fun getSavedGames(): Flow<List<SavedGame>> {
        return savedGamesDataSource.savedGamesFlow
    }

    suspend fun saveGame(gameType: GameType, seed: Long, position: Int, stackSize: Int) {
        savedGamesDataSource.saveGame(
            SavedGame(
                position = position,
                stackSize = stackSize,
                gameType = gameType,
                seed = seed,
                lastModified = System.currentTimeMillis()
            )
        )
    }

    suspend fun deleteSavedGame(seed: Long) {
        savedGamesDataSource.deleteGame(seed)
    }

    suspend fun deleteAllSavedGames() {
        savedGamesDataSource.clearSavedGames()
    }
}
