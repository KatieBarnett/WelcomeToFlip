package dev.veryniche.welcometoflip

import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.map
import androidx.lifecycle.viewModelScope
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.veryniche.welcometoflip.core.models.Action
import dev.veryniche.welcometoflip.core.models.Card
import dev.veryniche.welcometoflip.core.models.GameType
import dev.veryniche.welcometoflip.core.models.Letter
import dev.veryniche.welcometoflip.storage.SavedGamesRepository
import kotlinx.coroutines.launch
import kotlin.random.Random

@HiltViewModel(assistedFactory = SoloGameViewModel.SoloGameViewModelFactory::class)
class SoloGameViewModel @AssistedInject constructor(
    @Assisted override val gameType: GameType,
    @Assisted override val gameSeed: Long,
    @Assisted override val initialPosition: Int,
    private val deckRepository: DeckRepository,
    private val savedGamesRepository: SavedGamesRepository
) : GameViewModel(gameType, gameSeed, initialPosition, deckRepository, savedGamesRepository), DefaultLifecycleObserver {

    @AssistedFactory
    interface SoloGameViewModelFactory {
        fun create(gameType: GameType, gameSeed: Long, initialPosition: Int): SoloGameViewModel
    }

    companion object {
        private const val ACTIVE_CARD_COUNT = 3
    }

    val phase = position.map {
        if (it < 0) {
            SoloGamePhase.SETUP
        } else {
            SoloGamePhase.PLAY
        }
    }

    lateinit var soloEffectCards: List<Card>

    private val soloStack = mutableListOf<Card>()
    private val discardStack = mutableListOf<Card>()
    private val astraCards = mutableMapOf<Action, Int>()
    private val effectCardsDrawn = mutableListOf<Letter>()

    val currentState = MutableLiveData(SoloState())
    val activeCardToAstra = currentState.map {
        null // .it.activeCardsAvailable
    }

    init {
        // TODO - does super init get called?
        viewModelScope.launch {
            soloEffectCards = deckRepository.getSoloEffectCards(gameType)
        }
        setupSoloDrawStack()
        setupAstraCards()
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
                currentState.postValue(
                    state.copy(cardDiscardedToAstra = state.activeCards.getIndexOfCardNotInList(state.cardsDiscarded))
                )
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
