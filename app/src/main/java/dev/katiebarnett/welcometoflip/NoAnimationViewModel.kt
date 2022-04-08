package dev.katiebarnett.welcometoflip

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.katiebarnett.welcometoflip.models.Card
import javax.inject.Inject

@HiltViewModel
class NoAnimationViewModel @Inject constructor(
) : DeckViewModel() {
    
    private val currentDeckPosition = MutableLiveData(1)
    
    val numberDeckTop = Transformations.map(currentDeckPosition) {
        deck.getOrNull(it)?.front
    }

    val actionDeckTop = Transformations.map(currentDeckPosition) {
        deck.getOrNull(it - 1)?.back
    }
    
    fun nextCardDeckUpdate() {
        currentDeckPosition.value?.let {
            if (it < deck.size) {
                currentDeckPosition.postValue(it + 1)
            }
        }
    }

    fun previousCardDeckUpdate() {
        currentDeckPosition.value?.let {
            if (it > 0) {
                currentDeckPosition.postValue(it - 1)
            }
        }
    }
}