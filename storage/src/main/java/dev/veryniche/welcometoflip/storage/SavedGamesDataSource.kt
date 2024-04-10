package dev.veryniche.welcometoflip.storage

import androidx.datastore.core.DataStore
import dev.veryniche.welcometoflip.core.models.SavedGame
import dev.veryniche.welcometoflip.core.models.mapToGameType
import dev.veryniche.welcometoflip.storage.models.Game
import dev.veryniche.welcometoflip.storage.models.Savedgames
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class SavedGamesDataSource @Inject constructor(
    private val savedGamesStore: DataStore<Savedgames>
) {

    companion object {
        internal const val PROTO_FILE_NAME = "saved_games.pb"
    }

    val savedGamesFlow = savedGamesStore.data
        .map {
            it.gamesList.map {
                SavedGame(
                    position = it.position,
                    gameType = it.gameType.mapToGameType(),
                    seed = it.seed.toLong(),
                    lastModified = it.lastModified.toLong(),
                    stackSize = it.stackSize
                )
            }.sortedByDescending { it.lastModified }
        }

    suspend fun saveGame(game: SavedGame) {
        savedGamesStore.updateData { currentSavedGames ->
            val currentIndex =
                currentSavedGames.gamesList.indexOfFirst { it.seed.toLong() == game.seed }
            if (currentIndex != -1) {
                currentSavedGames.toBuilder().setGames(currentIndex, game.toGame()).build()
            } else {
                currentSavedGames.toBuilder().addGames(game.toGame()).build()
            }
        }
    }

    suspend fun deleteGame(seed: Long) {
        savedGamesStore.updateData { currentSavedGames ->
            val currentIndex = currentSavedGames.gamesList.indexOfFirst { it.seed.toLong() == seed }
            if (currentIndex != -1) {
                currentSavedGames.toBuilder().removeGames(currentIndex).build()
            } else {
                // Do nothing
                currentSavedGames.toBuilder().build()
            }
        }
    }

    suspend fun clearSavedGames() {
        savedGamesStore.updateData { currentSavedGames ->
            currentSavedGames.toBuilder().clearGames().build()
        }
    }

    fun SavedGame.toGame(): Game {
        val gameBuilder = Game.newBuilder()
        gameBuilder.gameType = gameType?.name
        gameBuilder.seed = seed.toDouble()
        gameBuilder.position = position
        gameBuilder.stackSize = stackSize
        gameBuilder.lastModified = lastModified.toDouble()
        return gameBuilder.build()
    }
}
