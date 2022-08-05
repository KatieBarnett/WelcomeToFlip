package dev.katiebarnett.welcometoflip

import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.Transformations
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.katiebarnett.welcometoflip.core.models.Card
import dev.katiebarnett.welcometoflip.core.models.GameType
import dev.katiebarnett.welcometoflip.storage.SavedGamesRepository
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.random.Random

@HiltViewModel
class SoloGameViewModel @Inject constructor(
    private val deckRepository: DeckRepository,
    private val savedGamesRepository: SavedGamesRepository
) : GameViewModel(deckRepository, savedGamesRepository), DefaultLifecycleObserver {

    val phase = Transformations.map(position) {
        if (it < 0) {
            SoloGamePhase.SETUP
        } else {
            SoloGamePhase.PLAY
        }
    }

    override val initialPosition: Int
        get() = -1
    
    lateinit var soloPile: List<Card>
    
    lateinit var soloStack: List<Card>
    
    override fun initialiseGame(gameType: GameType, gameSeed: Long, position: Int) {
        super.initialiseGame(gameType, gameSeed, position)
        if (!this::soloPile.isInitialized) {
            viewModelScope.launch {
                soloPile = deckRepository.getSoloDeck(gameType)
            }
        }
        if (position != initialPosition) {
            setupSoloStack()
        }
    }
    
    fun setupSoloStack() {
        viewModelScope.launch {
            val bottomStack = stacks.last().toMutableList()
            bottomStack.addAll(soloPile)
            val combinedStack = mutableListOf<Card>()
            stacks.forEachIndexed { index, stack ->
                combinedStack.addAll(
                    if (index != stacks.size - 1) {
                        stack
                    } else {
                        bottomStack.shuffled(Random(gameSeed)).toList()
                    }
                )
            }
            soloStack = combinedStack.toList()
        }
    }
}

enum class SoloGamePhase {
    SETUP, PLAY
}