package dev.katiebarnett.welcometoflip

import androidx.lifecycle.*
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.katiebarnett.welcometoflip.data.DeckRepository
import dev.katiebarnett.welcometoflip.data.GameType
import dev.katiebarnett.welcometoflip.models.Card
import dev.katiebarnett.welcometoflip.util.getStackSize
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.math.ceil
import kotlin.random.Random

@HiltViewModel
class GameViewModel @Inject constructor(
    private val deckRepository: DeckRepository
) : ViewModel() {
    
    companion object {
        private const val STACK_COUNT = 3f
    }

    lateinit var stacks: List<List<Card>>
    
    private val _position = MutableLiveData(0)
    val position: LiveData<Int> = _position

    val advancePositionEnabled = Transformations.map(position) {
        (stacks.getStackSize() ?: 0) > it
    }
    
    fun initialiseGame(gameType: GameType, gameSeed: Long) {
        // Only initialise if not already initialised
        if (!this::stacks.isInitialized) {
            viewModelScope.launch {
                val shuffledDeck = deckRepository.getDeck(gameType).shuffled(Random(gameSeed))
                val stackSize = ceil((shuffledDeck.size / STACK_COUNT).toDouble()).toInt()
                // TODO Handle unequal stacks
                stacks = shuffledDeck.chunked(stackSize)
            }
        }
    }
    
    fun advancePosition() {
        _position.postValue((_position.value ?: 0) + 1)
    }

    fun reshuffle() {
        stacks = stacks.map { it.shuffled() }
        _position.postValue(0)
    }
    
    
}