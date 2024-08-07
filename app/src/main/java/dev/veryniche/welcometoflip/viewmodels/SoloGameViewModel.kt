package dev.veryniche.welcometoflip.viewmodels

import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.veryniche.welcometoflip.DeckRepository
import dev.veryniche.welcometoflip.core.models.Action
import dev.veryniche.welcometoflip.core.models.Card
import dev.veryniche.welcometoflip.core.models.GameType
import dev.veryniche.welcometoflip.core.models.Letter
import dev.veryniche.welcometoflip.storage.SavedGamesRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlin.random.Random

@HiltViewModel(assistedFactory = SoloGameViewModel.SoloGameViewModelFactory::class)
class SoloGameViewModel @AssistedInject constructor(
    @Assisted val gameType: GameType,
    @Assisted val gameSeed: Long,
    @Assisted val initialPosition: Int,
    private val deckRepository: DeckRepository,
    private val savedGamesRepository: SavedGamesRepository
) : ViewModel(), DefaultLifecycleObserver {

    @AssistedFactory
    interface SoloGameViewModelFactory {
        fun create(gameType: GameType, gameSeed: Long, initialPosition: Int): SoloGameViewModel
    }

    companion object {
        private const val ACTIVE_CARD_COUNT = 3
    }

//    val phase = position.map {
//        if (it < 0) {
//            SoloGamePhase.SETUP
//        } else {
//            SoloGamePhase.PLAY
//        }
//    }

    private val _currentState = MutableStateFlow<SoloState>(SoloState())
    val currentState = _currentState.asStateFlow()

    init {
        viewModelScope.launch {
            val soloEffectCards = deckRepository.getSoloEffectCards(gameType)

            val soloStack = mutableListOf<Card>()
            val discardStack = mutableListOf<Card>()
            val shuffledDeck = deckRepository.getDeck(gameType).shuffled(Random(gameSeed))
            soloStack.addAll(shuffledDeck.subList(0, 20))
            val effectCards = soloEffectCards.shuffled()
            soloStack.add(effectCards.first())
            discardStack.addAll(effectCards.subList(1, 3))
            soloStack.shuffle()
            soloStack.addAll(shuffledDeck.subList(20, shuffledDeck.size))

            val astraCards = deckRepository.getAvailableActions(gameType).map {
                it to 0
            }.toMap()

            _currentState.emit(
                SoloState(
                    drawStack = soloStack,
                    discardStack = discardStack,
                    astraCards = astraCards
                )
            )
        }
    }


    suspend fun drawCards() {
        _currentState.emit(
            currentState.value.copy(
                activeCards = currentState.value.drawStack.take(ACTIVE_CARD_COUNT),
                drawStack = currentState.value.drawStack.drop(ACTIVE_CARD_COUNT)
            )
        )
    }
}

enum class SoloGamePhase {
    SETUP, PLAY
}

data class SoloState(
    val drawStack: List<Card> = listOf(),
    val discardStack: List<Card> = listOf(),
    val activeCards: List<Card> = listOf(),
    val astraCards: Map<Action, Int> = mapOf(),
    val effectCards: List<Letter> = listOf(),
) {
    val drawStackTopCard: Card?
        get() = drawStack.firstOrNull()

    val discardStackTopCard: Card?
        get() = discardStack.lastOrNull()
}

//
//    override fun advancePosition(): Int {
//        val newPosition = super.advancePosition()
//        val stackGroups = soloStack.chunked(ACTIVE_CARD_COUNT)
//        currentState.postValue(
//            SoloState(
//                drawStackTopCard = stackGroups.getOrNull(newPosition + 1)?.firstOrNull(),
//                discardStackTopCard = discardStack.lastOrNull(),
//                activeCards = stackGroups.getOrNull(newPosition) ?: listOf(),
//                astraCards = astraCards,
//                effectCards = effectCardsDrawn,
//                totalPosition = stackGroups.size,
//                cardsDiscarded = listOf()
// //                activeCardsAvailable = null //ACTIVE_CARD_COUNT
//            )
//        )
//        return newPosition
//    }
//
//    fun handleActiveCardClick(index: Int) {
//        currentState.value?.let { state ->
//            state.activeCards.getOrNull(index)?.let { discardedCard ->
//                discardStack.add(discardedCard)
//                currentState.postValue(state.copy(cardsDiscarded = state.cardsDiscarded.plus(index)))
//            }
//        }
//    }
//
//    fun checkRemainingActiveCards() {
//        currentState.value?.let { state ->
//            if (state.cardsDiscarded.size == state.activeCards.size - 1) {
//                currentState.postValue(
//                    state.copy(cardDiscardedToAstra = state.activeCards.getIndexOfCardNotInList(state.cardsDiscarded))
//                )
//            }
//        }
//    }
//
//    fun List<Any>.getIndexOfCardNotInList(checkList: List<Int>): Int? {
//        forEachIndexed { index, _ ->
//            if (!checkList.contains(index)) {
//                return index
//            }
//        }
//        return null
//    }
