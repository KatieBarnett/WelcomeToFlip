package dev.veryniche.welcometoflip

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.veryniche.welcometoflip.core.models.SavedGame
import dev.veryniche.welcometoflip.storage.SavedGamesRepository
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val deckRepository: DeckRepository,
    private val savedGamesRepository: SavedGamesRepository
) : ViewModel() {

    val showWelcomeDialog = MutableLiveData(true)

    val gameTypes = deckRepository.getAvailableGames()
    
    val savedGames = savedGamesRepository.getSavedGames()
    
    fun deleteGameAction(savedGame: SavedGame) {
        viewModelScope.launch {
            savedGamesRepository.deleteSavedGame(savedGame.seed)
        }
    } 
}