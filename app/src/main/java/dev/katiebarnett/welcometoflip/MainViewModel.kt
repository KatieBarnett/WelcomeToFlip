package dev.katiebarnett.welcometoflip

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.katiebarnett.welcometoflip.data.DeckRepository
import dev.katiebarnett.welcometoflip.data.GameType
import dev.katiebarnett.welcometoflip.data.WelcomeToTheMoon
import dev.katiebarnett.welcometoflip.models.Card
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
) : ViewModel() {

    val games = listOf(WelcomeToTheMoon)
}