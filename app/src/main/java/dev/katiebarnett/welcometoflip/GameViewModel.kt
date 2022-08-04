package dev.katiebarnett.welcometoflip

import androidx.lifecycle.*
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.katiebarnett.welcometoflip.core.models.Card
import dev.katiebarnett.welcometoflip.core.models.GameType
import dev.katiebarnett.welcometoflip.storage.SavedGamesRepository
import dev.katiebarnett.welcometoflip.util.getStackSize
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.math.ceil
import kotlin.random.Random

@HiltViewModel
open class GameViewModel @Inject constructor(
    private val deckRepository: DeckRepository,
    private val savedGamesRepository: SavedGamesRepository
) : ViewModel(), DefaultLifecycleObserver {
    
    companion object {
        private const val STACK_COUNT = 3f
    }

    lateinit var gameType: GameType
    private var gameSeed: Long = 0L
    lateinit var stacks: List<List<Card>>
    
    open val initialPosition
        get() = 0
    
    private val _position = MutableLiveData(initialPosition)
    val position: LiveData<Int> = _position
    
    private var shouldSaveGameOnPause = true

    val advancePositionEnabled = Transformations.map(position) {
        (stacks.getStackSize() ?: 0) > it + 1
    }

    val isEndGame = Transformations.map(advancePositionEnabled) {
        !it
    }
    
    open fun initialiseGame(gameType: GameType, gameSeed: Long, position: Int) {
        // Only initialise if not already initialised
        if (!this::stacks.isInitialized) {
            this.gameType = gameType
            this.gameSeed = gameSeed
            this._position.postValue(position)
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

    fun reshuffleStacks() {
        stacks = stacks.map { it.shuffled() }
        _position.postValue(0)
    }
    
    fun endGame(onEnd: () -> Unit) {
        shouldSaveGameOnPause = false
        viewModelScope.launch {
            savedGamesRepository.deleteSavedGame(gameSeed)
            onEnd.invoke()
        }
    }
    
    fun saveGame() {
        viewModelScope.launch {
            savedGamesRepository.saveGame(
                gameType = gameType,
                position = position.value ?: 0,
                seed = gameSeed,
                stackSize = stacks.getStackSize() ?: 0
            )
        }
    }

    override fun onPause(owner: LifecycleOwner) {
        super.onPause(owner)
        if (shouldSaveGameOnPause) {
            saveGame()
        }
    }
    
    

}