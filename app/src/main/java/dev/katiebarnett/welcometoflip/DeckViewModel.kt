package dev.katiebarnett.welcometoflip

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.katiebarnett.welcometoflip.models.Card
import javax.inject.Inject

@HiltViewModel
open class DeckViewModel @Inject constructor(
) : ViewModel() {
    
    protected val deck = listOf(
        Card(back = R.drawable.action_1, front = R.drawable.number_11), 
        Card(R.drawable.action_2, R.drawable.number_12),
        Card(R.drawable.action_3, R.drawable.number_6),
        Card(R.drawable.action_4, R.drawable.number_8)
    )
}