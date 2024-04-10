package dev.veryniche.welcometoflip

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.veryniche.welcometoflip.core.models.Card
import javax.inject.Inject

@HiltViewModel
open class StackViewModel @Inject constructor(
) : ViewModel() {
    
    private lateinit var stack: List<Card>
    private var position: Int = 0
    
    val numberStackTop
        get() = stack.getOrNull(position + 1)
    
    val actionStackTop
        get() = stack.getOrNull(position - 1)

    val currentCard
        get() = stack.getOrNull(position)

    fun setPosition(newPosition: Int) {
        position = newPosition
    }

    fun setStack(newStack: List<Card>) {
        stack = newStack
    }
}