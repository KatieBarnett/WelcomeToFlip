package dev.katiebarnett.welcometoflip

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class JetpackComposeViewModel @Inject constructor(
) : DeckViewModel() {


    private val numberDeckTopPosition = MutableLiveData(1)
    private val actionDeckTopPosition = MutableLiveData(0)

    private val flipCardTopPosition = MutableLiveData(1)
    private val flipCardBottomPosition = MutableLiveData(1)

    val numberDeckTop = Transformations.map(numberDeckTopPosition) {
        deck.getOrNull(it)?.number
    }

    val actionDeckTop = Transformations.map(actionDeckTopPosition) {
        deck.getOrNull(it)?.action
    }

    val flipCardFront = Transformations.map(flipCardTopPosition) {
        deck.getOrNull(it)?.number
    }

    val flipCardBack = Transformations.map(flipCardBottomPosition) {
        deck.getOrNull(it)?.action
    }

    fun nextCardNumberDeck() {
        numberDeckTopPosition.value?.let {
            if (it < deck.size) {
                numberDeckTopPosition.postValue(it + 1)
            }
        }
    }

    fun nextCardActionDeck() {
        actionDeckTopPosition.value?.let {
            if (it < deck.size) {
                actionDeckTopPosition.postValue(it + 1)
            }
        }
    }

    fun previousCardNumberDeck() {
        numberDeckTopPosition.value?.let {
            if (it > 0) {
                numberDeckTopPosition.postValue(it - 1)
            }
        }
    }

    fun previousCardActionDeck() {
        actionDeckTopPosition.value?.let {
            if (it > 0) {
                actionDeckTopPosition.postValue(it - 1)
            }
        }
    }

    fun updateFlipCardPosition() {
        flipCardTopPosition.postValue(numberDeckTopPosition.value)
        flipCardBottomPosition.postValue(numberDeckTopPosition.value)
    }
}