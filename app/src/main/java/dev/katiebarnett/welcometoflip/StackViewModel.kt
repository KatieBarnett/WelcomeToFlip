package dev.katiebarnett.welcometoflip

import android.util.Log
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.katiebarnett.welcometoflip.models.Card
import javax.inject.Inject

@HiltViewModel
open class StackViewModel @Inject constructor(
) : ViewModel() {
    
    private lateinit var stack: List<Card>
    private var position: Int = 0
    
//    private val numberDeckTopPosition = MutableLiveData(1)
//    private val actionDeckTopPosition = MutableLiveData(0)
//
//    private val flipCardTopPosition = MutableLiveData(1)
//    private val flipCardBottomPosition = MutableLiveData(1)
//
    val numberStackTop
        get() = stack.getOrNull(position)
    
    val actionStackTop
        get() = stack.getOrNull(position - 1)

    val nextNumberCard
        get() = stack.getOrNull(position + 1)

//    val flipCardFront
//        get() = stack.getOrNull(position)?.number
//    
//    val flipCardBack
//        get() = stack.getOrNull(position)?.action

    fun setPosition(newPosition: Int) {
        position = newPosition
    }

    fun setStack(newStack: List<Card>) {
        stack = newStack
    }
    
    
    
    init {
        
        Log.d("TEST", "does this happen every change?")
    }

//    fun nextCardNumberDeck() {
//        numberDeckTopPosition.value?.let {
//            if (it < (stack.value?.size ?: 0)) {
//                numberDeckTopPosition.postValue(it + 1)
//            }
//        }
//    }
//
//    fun nextCardActionDeck() {
//        actionDeckTopPosition.value?.let {
//            if (it < (stack.value?.size ?: 0)) {
//                actionDeckTopPosition.postValue(it + 1)
//            }
//        }
//    }
//
//    fun previousCardNumberDeck() {
//        numberDeckTopPosition.value?.let {
//            if (it > 0) {
//                numberDeckTopPosition.postValue(it - 1)
//            }
//        }
//    }
//
//    fun previousCardActionDeck() {
//        actionDeckTopPosition.value?.let {
//            if (it > 0) {
//                actionDeckTopPosition.postValue(it - 1)
//            }
//        }
//    }
//
//    fun updateFlipCardPosition() {
//        flipCardTopPosition.postValue(numberDeckTopPosition.value)
//        flipCardBottomPosition.postValue(numberDeckTopPosition.value)
//    }
}