package dev.katiebarnett.welcometoflip

import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.katiebarnett.welcometoflip.core.models.Action
import dev.katiebarnett.welcometoflip.core.models.Card
import dev.katiebarnett.welcometoflip.core.models.GameType
import dev.katiebarnett.welcometoflip.core.models.Letter
import dev.katiebarnett.welcometoflip.storage.SavedGamesRepository
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.random.Random

@HiltViewModel
class SoloGameViewModel @Inject constructor(
    private val deckRepository: DeckRepository,
    private val savedGamesRepository: SavedGamesRepository
) : GameViewModel(deckRepository, savedGamesRepository), DefaultLifecycleObserver {

    companion object {
        private const val ACTIVE_CARD_COUNT = 3
    }
    
    val phase = Transformations.map(position) {
        if (it < 0) {
            SoloGamePhase.SETUP
        } else {
            SoloGamePhase.PLAY
        }
    }

    override val initialPosition: Int
        get() = -1
    
    lateinit var soloEffectCards: List<Card>
    
    private val soloStack = mutableListOf<Card>()
    private val discardStack = mutableListOf<Card>()
    private val astraCards = mutableMapOf<Action, Int>()
    private val effectCardsDrawn = mutableListOf<Letter>()
    
    val currentState = MutableLiveData(SoloState())
    val activeCardToAstra = Transformations.map(currentState) {
        null//.it.activeCardsAvailable
    }
    
    override fun initialiseGame(gameType: GameType, gameSeed: Long, position: Int) {
        super.initialiseGame(gameType, gameSeed, position)
        if (!this::soloEffectCards.isInitialized) {
            viewModelScope.launch {
                soloEffectCards = deckRepository.getSoloEffectCards(gameType)
            }
        }
        if (position != initialPosition) {
            setupSoloDrawStack()
            setupAstraCards()
        }
    }
    
    fun setupSoloDrawStack() {
        viewModelScope.launch {
            val bottomStack = stacks.last().toMutableList()
            bottomStack.addAll(soloEffectCards)
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
            soloStack.clear()
            soloStack.addAll(combinedStack)
        }
    }
    
    fun setupAstraCards() {
        astraCards.clear()
        deckRepository.getAvailableActions(gameType).forEach {
            astraCards[it] = 0
        }
    }

    override fun advancePosition(): Int {
        val newPosition = super.advancePosition()
        val stackGroups = soloStack.chunked(ACTIVE_CARD_COUNT)
        currentState.postValue(
            SoloState(
                drawStackTopCard = stackGroups.getOrNull(newPosition + 1)?.firstOrNull(),
                discardStackTopCard = discardStack.lastOrNull(),
                activeCards = stackGroups.getOrNull(newPosition) ?: listOf(),
                astraCards = astraCards,
                effectCards = effectCardsDrawn,
                totalPosition = stackGroups.size,
                cardsDiscarded = listOf()
//                activeCardsAvailable = null //ACTIVE_CARD_COUNT
            )
        )
        return newPosition
    }
    
    fun handleActiveCardClick(index: Int) {
        currentState.value?.let { state ->
            state.activeCards.getOrNull(index)?.let { discardedCard ->
                discardStack.add(discardedCard)
                currentState.postValue(state.copy(cardsDiscarded = state.cardsDiscarded.plus(index)))
            }
        }
    }
    
    fun checkRemainingActiveCards() {
        currentState.value?.let { state ->
            if (state.cardsDiscarded.size == state.activeCards.size - 1) {
                currentState.postValue(state.copy(cardDiscardedToAstra = state.activeCards.getIndexOfCardNotInList(state.cardsDiscarded)))
            }
        }
    }
    
    fun List<Any>.getIndexOfCardNotInList(checkList: List<Int>): Int? {
        forEachIndexed { index, _ -> 
            if (!checkList.contains(index)) {
                return index
            }
        }
        return null
    }
}

enum class SoloGamePhase {
    SETUP, PLAY
}

data class SoloState(
    val drawStackTopCard: Card? = null,
    val discardStackTopCard: Card? = null,
    val activeCards: List<Card> = listOf(),
    val astraCards: Map<Action, Int> = mapOf(),
    val effectCards: List<Letter> = listOf(),
    val totalPosition: Int = 0,

    val cardsDiscarded: List<Int> = listOf(),
    val cardDiscardedToAstra: Int? = null
)