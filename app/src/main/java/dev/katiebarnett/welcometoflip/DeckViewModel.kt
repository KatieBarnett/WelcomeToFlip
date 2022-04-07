package dev.katiebarnett.welcometoflip

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.katiebarnett.welcometoflip.models.Card
import javax.inject.Inject

@HiltViewModel
class DeckViewModel @Inject constructor(
) : ViewModel() {
    
    private val deck = listOf(
        Card(R.drawable.action_1, R.drawable.number_11), 
        Card(R.drawable.action_2, R.drawable.number_12),
        Card(R.drawable.action_3, R.drawable.number_6),
        Card(R.drawable.action_4, R.drawable.number_8)
    )
    
    private val currentPosition = MutableLiveData(1)
    
    val numberDeckTop = Transformations.map(currentPosition) {
        deck.getOrNull(it)?.front
    }

    val actionDeckTop = Transformations.map(currentPosition) {
        deck.getOrNull(it - 1)?.back
    }
    
    fun nextCardDeckUpdate() {
        currentPosition.value?.let {
            if (it < deck.size) {
                currentPosition.postValue(it + 1)
            }
        }
    }

    fun previousCardDeckUpdate() {
        currentPosition.value?.let {
            if (it > 0) {
                currentPosition.postValue(it - 1)
            }
        }
    }
    
}