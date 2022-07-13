package dev.katiebarnett.welcometoflip

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.katiebarnett.welcometoflip.data.WelcomeToTheMoon
import dev.katiebarnett.welcometoflip.models.Card
import javax.inject.Inject

@HiltViewModel
open class DeckViewModel @Inject constructor(
) : ViewModel() {
    
    protected val deck = listOf<Card>()
}