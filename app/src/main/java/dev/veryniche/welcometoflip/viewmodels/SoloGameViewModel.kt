package dev.veryniche.welcometoflip.viewmodels

import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.veryniche.welcometoflip.DeckRepository
import dev.veryniche.welcometoflip.core.models.Card
import dev.veryniche.welcometoflip.core.models.GameType
import dev.veryniche.welcometoflip.core.models.Letter
import dev.veryniche.welcometoflip.core.models.WelcomeToClassic
import dev.veryniche.welcometoflip.core.models.WelcomeToTheMoon
import dev.veryniche.welcometoflip.storage.SavedGamesRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlin.collections.chunked
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

    private val _currentState = MutableStateFlow(SoloState(gameType = gameType))
    val currentState = _currentState.asStateFlow()

    init {
        viewModelScope.launch {

            val (drawStack, discardStack) = initialiseDecks()

            _currentState.emit(
                SoloState(
                    drawStack = drawStack,
                    discardStack = discardStack,
                    aiStack = listOf<Card>(),
                    gameType = gameType,
                )
            )
        }
    }

    fun initialiseDecks(): Pair<List<Card>, List<Card>> {
        val soloEffectCards = deckRepository.getSoloEffectCards(gameType)
        val shuffledDeck = deckRepository.getDeck(gameType).shuffled(Random(gameSeed))
        val drawStack = mutableListOf<Card>()
        val discardStack = mutableListOf<Card>()
        val effectCards = soloEffectCards.shuffled()
        when (gameType) {
            WelcomeToClassic -> {
                drawStack.addAll(shuffledDeck.take(20))
                drawStack.add(effectCards.first())
                discardStack.addAll(effectCards.drop(1))
                drawStack.shuffle()
                drawStack.addAll(shuffledDeck.drop(20))
            }
            WelcomeToTheMoon -> {
                val chunkedDeck = shuffledDeck.chunked(shuffledDeck.size / 3)
                drawStack.addAll(chunkedDeck.first())
                drawStack.addAll(effectCards)
                drawStack.shuffle()
                for (i in 1..chunkedDeck.size - 1) {
                    drawStack.addAll(chunkedDeck[i])
                }
            }
        }
        return drawStack to discardStack
    }

    // Draw three cards
    suspend fun drawCards() {
        val gameEnd = if (currentState.value.drawStack.size < ACTIVE_CARD_COUNT) {
            handleEmptyDrawDeck()
        } else {
            false
        }
        if (!gameEnd) {
            var activeCards = currentState.value.drawStack.take(ACTIVE_CARD_COUNT)
            var consumedCards = ACTIVE_CARD_COUNT
            while (activeCards.any { card -> card.action is Letter }) {
                val effectCard = activeCards.first { card -> card.action is Letter }
                activeCards = activeCards.minus(effectCard)
                currentState.value.drawStack.getOrNull(consumedCards)?.let {
                    activeCards = activeCards.plus(it)
                }
                consumedCards++
                _currentState.emit(
                    currentState.value.copy(
                        discardStack = currentState.value.discardStack.plus(effectCard),
                        phase = SoloGamePhase.EffectCardDrawn(effectCard),
                    )
                )
            }
            val drawStack = currentState.value.drawStack.drop(consumedCards)
            _currentState.emit(
                currentState.value.copy(
                    activeCards = activeCards,
                    drawStack = drawStack,
                    phase = SoloGamePhase.DrawCards,
                )
            )
        }
    }

    // Check for effect card
    suspend fun handleEffectCard() {
    }

    // Player clicks on player card
    suspend fun selectPlayerCard(card: Card) {
    }

    // Remaining card for AAA/Astra
    suspend fun selectAiCard(card: Card) {
    }

    suspend fun handleEmptyDrawDeck(): Boolean {
        val currentState = currentState.value
        return when (gameType) {
            WelcomeToClassic -> {
                // Just reshuffle discard pile
                _currentState.emit(
                    currentState.copy(
                        drawStack = listOf(),
                        discardStack = currentState.drawStack.shuffled(),
                        reshuffleCount = currentState.reshuffleCount + 1,
                        phase = SoloGamePhase.Reshuffle,
                    )
                )
                false // game end
            }
            WelcomeToTheMoon -> {
                // First time reshuffle discard pile
                if (currentState.reshuffleCount == 0) {
                    _currentState.emit(
                        currentState.copy(
                            drawStack = listOf(),
                            discardStack = currentState.drawStack.shuffled(),
                            reshuffleCount = currentState.reshuffleCount + 1,
                            phase = SoloGamePhase.Reshuffle,
                        )
                    )
                    false // game end
                } else {
                    _currentState.emit(currentState.copy(phase = SoloGamePhase.EndGame))
                    true // game end
                }
            }
            else -> {
                true // Weird state
            }
        }
    }

    // Game end choice
    suspend fun handleGameEnd() {
        _currentState.emit(currentState.value.copy(phase = SoloGamePhase.EndGame))
    }
}

sealed class SoloGamePhase(val name: String) {
    object Setup : SoloGamePhase(name = "Setup")

    object DrawCards : SoloGamePhase(name = "DrawCards")
    object SelectCards : SoloGamePhase(name = "SelectCards")
    data class EffectCardDrawn(val card: Card) : SoloGamePhase(name = "EffectCardDrawn")
    object PlayerSelection : SoloGamePhase(name = "PlayerSelection") // TODO pass in which selections have been made
    data class AiSelection(val card: Card) : SoloGamePhase(name = "AiSelection")
    object Reshuffle : SoloGamePhase(name = "Reshuffle")
    object EndGame : SoloGamePhase(name = "EndGame")
}

data class SoloState(
    val phase: SoloGamePhase = SoloGamePhase.Setup,
    val drawStack: List<Card> = listOf(),
    val discardStack: List<Card> = listOf(),
    val activeCards: List<Card> = listOf(),
    val drawnEffectCards: List<Letter> = listOf(),
    val aiStack: List<Card> = listOf(),
    val gameType: GameType,
    val reshuffleCount: Int = 0,
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
