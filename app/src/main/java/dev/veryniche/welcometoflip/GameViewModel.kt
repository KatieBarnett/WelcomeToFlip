package dev.veryniche.welcometoflip

import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.map
import androidx.lifecycle.viewModelScope
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.veryniche.welcometoflip.core.models.Card
import dev.veryniche.welcometoflip.core.models.GameType
import dev.veryniche.welcometoflip.storage.SavedGamesRepository
import dev.veryniche.welcometoflip.util.getStackSize
import kotlinx.coroutines.launch
import kotlin.math.ceil
import kotlin.random.Random

@HiltViewModel(assistedFactory = GameViewModel.GameViewModelFactory::class)
open class GameViewModel @AssistedInject constructor(
    @Assisted open val gameType: GameType,
    @Assisted open val gameSeed: Long,
    @Assisted open val initialPosition: Int,
    private val deckRepository: DeckRepository,
    private val savedGamesRepository: SavedGamesRepository
) : ViewModel(), DefaultLifecycleObserver {

    @AssistedFactory
    interface GameViewModelFactory {
        fun create(gameType: GameType, gameSeed: Long, initialPosition: Int): GameViewModel
    }

    companion object {
        private const val STACK_COUNT = 3
    }

    var stacks: List<List<Card>> = listOf()

    private val _position = MutableLiveData(initialPosition)
    val position: LiveData<Int> = _position

    private var shouldSaveGameOnPause = true

    val advancePositionEnabled = position.map {
        (stacks.getStackSize() ?: 0) > it + 1
    }

    val isEndGame = advancePositionEnabled.map {
        !it
    }

    init {
        viewModelScope.launch {
            val shuffledDeck = deckRepository.getDeck(gameType).shuffled(Random(gameSeed))
            val stackSize = ceil((shuffledDeck.size.toFloat() / STACK_COUNT).toDouble()).toInt()
            // TODO Handle unequal stacks
            stacks = shuffledDeck.chunked(stackSize)
        }
    }

    open fun advancePosition(): Int {
        val newPosition = (_position.value ?: 0) + 1
        _position.postValue(newPosition)
        return newPosition
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
