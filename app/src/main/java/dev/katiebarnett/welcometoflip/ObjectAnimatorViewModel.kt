package dev.katiebarnett.welcometoflip

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.katiebarnett.welcometoflip.models.Card
import javax.inject.Inject

@HiltViewModel
class ObjectAnimatorViewModel @Inject constructor(
) : DeckViewModel() {
    
    private val numberDeckTopPosition = MutableLiveData(1)
    private val actionDeckTopPosition = MutableLiveData(0)
    
    private val flipCardTopPosition = MutableLiveData(1)
    private val flipCardBottomPosition = MutableLiveData(1)
    
    val numberDeckTop = Transformations.map(numberDeckTopPosition) {
        deck.getOrNull(it)?.front
    }

    val actionDeckTop = Transformations.map(actionDeckTopPosition) {
        deck.getOrNull(it)?.back
    }

    val flipCardFront = Transformations.map(flipCardTopPosition) {
        deck.getOrNull(it)?.front
    }

    val flipCardBack = Transformations.map(flipCardBottomPosition) {
        deck.getOrNull(it)?.back
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