package dev.veryniche.welcometoflip

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.veryniche.welcometoflip.core.models.SavedGame
import dev.veryniche.welcometoflip.storage.SavedGamesRepository
import dev.veryniche.welcometoflip.storage.UserPreferencesRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val deckRepository: DeckRepository,
    private val savedGamesRepository: SavedGamesRepository,
    private val userPreferencesRepository: UserPreferencesRepository
) : ViewModel() {

    private val userPreferencesFlow = userPreferencesRepository.userPreferencesFlow

    val showWelcomeDialog = userPreferencesFlow.map { it.showWelcomeOnStart }

    fun updateShowWelcomeOnStart(value: Boolean) {
        viewModelScope.launch(Dispatchers.IO) {
            userPreferencesRepository.updateShowWelcomeOnStart(value)
        }
    }

    val gameTypes = deckRepository.getAvailableGames()
    
    val savedGames = savedGamesRepository.getSavedGames()
    
    fun deleteGameAction(savedGame: SavedGame) {
        viewModelScope.launch {
            savedGamesRepository.deleteSavedGame(savedGame.seed)
        }
    } 
}