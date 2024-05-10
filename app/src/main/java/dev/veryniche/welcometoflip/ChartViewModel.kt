package dev.veryniche.welcometoflip

import androidx.lifecycle.ViewModel
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.veryniche.welcometoflip.core.models.GameType

@HiltViewModel(assistedFactory = ChartViewModel.ChartViewModelFactory::class)
open class ChartViewModel @AssistedInject constructor(
    @Assisted val gameType: GameType,
    deckRepository: DeckRepository,
) : ViewModel() {

    @AssistedFactory
    interface ChartViewModelFactory {
        fun create(gameType: GameType): ChartViewModel
    }

    val deck = deckRepository.getDeck(gameType)
}
