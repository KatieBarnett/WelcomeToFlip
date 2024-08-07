package dev.veryniche.welcometoflip.viewmodels

import android.app.Activity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.veryniche.welcometoflip.BuildConfig
import dev.veryniche.welcometoflip.DeckRepository
import dev.veryniche.welcometoflip.core.models.GameType
import dev.veryniche.welcometoflip.core.models.SavedGame
import dev.veryniche.welcometoflip.purchase.Products
import dev.veryniche.welcometoflip.purchase.PurchaseManager
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

    val showWelcomeDialog = userPreferencesFlow.map {
        it.showWelcomeOnStart
    }

    val keepScreenOn = userPreferencesFlow.map {
        it.keepScreenOn
    }

    private val gameTypes = deckRepository.getAvailableGames()

    val games = purchaseManager.availableProducts
        .combine(purchaseManager.purchases) { availableProducts, purchases ->
            val bundlePurchased = purchases.contains(Products.bundle) || BuildConfig.DEBUG
            gameTypes.map { game ->
                game.copy(
                    purchased = bundlePurchased || game.purchased || purchases.contains(game.mapToProductId(false)),
                    purchasePrice = availableProducts.find {
                        it.productId == game.mapToProductId(false)
                    }?.displayedPrice,
                    soloPurchased = bundlePurchased || game.soloPurchased || purchases.contains(game.mapToProductId(true))
                )
            }
        }

    val availableInAppProducts = purchaseManager.availableProducts
        .combine(purchaseManager.purchases) { availableProducts, purchases ->
            availableProducts.associate {
                it.productId to it.copy(purchased = purchases.contains(it.productId))
            }
        }

    val showAds = purchaseManager.purchases.map {
        (!it.contains(Products.adRemoval) && !it.contains(Products.bundle)) || BuildConfig.DEBUG
    }

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

    fun updateKeepScreenOn(value: Boolean) {
        viewModelScope.launch(Dispatchers.IO) {
            userPreferencesRepository.updateKeepScreenOn(value)
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
