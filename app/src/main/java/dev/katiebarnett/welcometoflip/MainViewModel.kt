package dev.katiebarnett.welcometoflip

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.katiebarnett.welcometoflip.data.WelcomeToTheMoon
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
) : ViewModel() {

    val games = listOf(WelcomeToTheMoon)
}