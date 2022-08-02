package dev.katiebarnett.welcometoflip.storage

import dev.katiebarnett.welcometoflip.core.models.GameType
import dev.katiebarnett.welcometoflip.core.models.SavedGame
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
    
    suspend fun saveGame(gameType: GameType, seed: Long, position: Int) {
        savedGamesDataSource.saveGame(SavedGame(
            position = position, 
            gameType = gameType,
            seed = seed,
            lastModified = System.currentTimeMillis()
        ))
    }
    
    suspend fun deleteSavedGame(seed: Long) {
        savedGamesDataSource.deleteGame(seed)
    }
    
    suspend fun deleteAllSavedGames() {
        savedGamesDataSource.clearSavedGames()
    }
}
