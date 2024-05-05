package dev.veryniche.welcometoflip

import android.app.Activity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.veryniche.welcometoflip.core.models.GameType
import dev.veryniche.welcometoflip.core.models.SavedGame
import dev.veryniche.welcometoflip.purchase.Products
import dev.veryniche.welcometoflip.purchase.PurchaseManager
import dev.veryniche.welcometoflip.purchase.PurchaseStatus
import dev.veryniche.welcometoflip.purchase.mapToProductId
import dev.veryniche.welcometoflip.review.ReviewManager
import dev.veryniche.welcometoflip.storage.SavedGamesRepository
import dev.veryniche.welcometoflip.storage.UserPreferencesRepository
import dev.veryniche.welcometoflip.util.trackPurchaseClick
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

@HiltViewModel(assistedFactory = MainViewModel.MainViewModelFactory::class)
class MainViewModel @AssistedInject constructor(
    @Assisted val purchaseManager: PurchaseManager,
    private val deckRepository: DeckRepository,
    private val savedGamesRepository: SavedGamesRepository,
    private val userPreferencesRepository: UserPreferencesRepository
) : ViewModel() {

    @AssistedFactory
    interface MainViewModelFactory {
        fun create(purchaseManager: PurchaseManager): MainViewModel
    }

    private val userPreferencesFlow = userPreferencesRepository.userPreferencesFlow

    val showWelcomeDialog = userPreferencesFlow.map { it.showWelcomeOnStart }

    private val gameTypes = deckRepository.getAvailableGames()

    val games = purchaseManager.availableProducts
        .combine(purchaseManager.purchases) { availableProducts, purchases ->
            gameTypes.map { game ->
                game.copy(
                    purchased = game.purchased || purchases.contains(game.mapToProductId(false)),
                    purchasePrice = availableProducts.find {
                        it.productId == game.mapToProductId(false)
                    }?.displayedPrice,
                    soloPurchased = game.soloPurchased || purchases.contains(game.mapToProductId(true))
                )
            }
        }

    val aboutPurchaseStatus = purchaseManager.availableProducts
        .combine(purchaseManager.purchases) { availableProducts, purchases ->
            availableProducts.filter {
                listOf(Products.adRemoval).contains(it.productId)
            }.associate {
                it.productId to
                    PurchaseStatus(
                        productId = it.productId,
                        purchasePrice = it.displayedPrice,
                        purchased = purchases.contains(it.productId)
                    )
            }
        }

    val showAds = purchaseManager.purchases.map { !it.contains(Products.adRemoval) }

    init {
        viewModelScope.launch {
            purchaseManager.connectToBilling()
        }
    }

    fun purchaseGame(game: GameType, solo: Boolean, onError: (message: Int) -> Unit) {
        purchaseProduct(game.mapToProductId(solo), onError)
    }

    fun purchaseProduct(productId: String, onError: (message: Int) -> Unit) {
        viewModelScope.launch {
            trackPurchaseClick(productId)
            purchaseManager.purchase(
                productId = productId,
                onError = onError
            )
        }
    }

    private val reviewManager = ReviewManager(userPreferencesRepository)

    fun requestReviewIfAble(activity: Activity) {
        viewModelScope.launch {
            reviewManager.requestReviewIfAble(activity, this)
        }
    }

    fun updateShowWelcomeOnStart(value: Boolean) {
        viewModelScope.launch(Dispatchers.IO) {
            userPreferencesRepository.updateShowWelcomeOnStart(value)
        }
    }

    val savedGames = savedGamesRepository.getSavedGames()
    // TODO only show last game?
//        .map {
//            it.distinctBy {
//                "${it.gameType?.displayName}${it.solo}"
//            }
//        }

    fun deleteGameAction(savedGame: SavedGame) {
        viewModelScope.launch {
            savedGamesRepository.deleteSavedGame(savedGame.seed)
        }
    }
}
