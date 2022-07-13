package dev.katiebarnett.welcometoflip

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.katiebarnett.welcometoflip.data.DeckRepository
import dev.katiebarnett.welcometoflip.data.GameType
import dev.katiebarnett.welcometoflip.models.Card
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class GameViewModel @Inject constructor(
    private val deckRepository: DeckRepository
) : ViewModel() {

    lateinit var stacks: List<List<Card>>
    
    fun initialiseGame(gameType: GameType) {

        viewModelScope.launch {
            stacks = listOf(deckRepository.getDeck(gameType))
        }
        
    }
}