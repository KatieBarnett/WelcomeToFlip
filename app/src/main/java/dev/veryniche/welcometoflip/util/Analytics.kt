package dev.veryniche.welcometoflip.util

import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.ktx.analytics
import com.google.firebase.analytics.ktx.logEvent
import com.google.firebase.ktx.Firebase
import dev.veryniche.welcometoflip.core.models.GameType
import dev.veryniche.welcometoflip.core.models.SavedGame
import dev.veryniche.welcometoflip.util.Analytics.Action.DeleteGame
import dev.veryniche.welcometoflip.util.Analytics.Action.EndGame
import dev.veryniche.welcometoflip.util.Analytics.Action.LoadGame
import dev.veryniche.welcometoflip.util.Analytics.Action.PurchaseClick
import dev.veryniche.welcometoflip.util.Analytics.Action.ReviewRequested
import dev.veryniche.welcometoflip.util.Analytics.Action.ShuffleGame
import timber.log.Timber

object Analytics {
    object Screen {
        const val ChooseGame = "Choose Game"
        const val About = "About"
        const val Charts = "Charts"
        const val Shop = "Shop"
    }

    object Action {
        const val LoadGame = "Load Game"
        const val DeleteGame = "Delete Game"
        const val ShuffleGame = "Shuffle Game"
        const val EndGame = "End Game"
        const val PurchaseClick = "Purchase Click"
        const val BannerAdClick = "Banner Ad Click"
        const val InterstitialAdClick = "Interstitial Ad Click: "
        const val ReviewRequested = "Review Requested"
    }
}

@Composable
fun TrackedScreen(
    lifecycleOwner: LifecycleOwner = LocalLifecycleOwner.current,
    onStart: () -> Unit, // Send the 'started' analytics event
) {
    if (!LocalInspectionMode.current) {
        // Safely update the current lambdas when a new one is provided
        val currentOnStart by rememberUpdatedState(onStart)

        // If `lifecycleOwner` changes, dispose and reset the effect
        DisposableEffect(lifecycleOwner) {
            // Create an observer that triggers our remembered callbacks
            // for sending analytics events
            val observer = LifecycleEventObserver { _, event ->
                if (event == Lifecycle.Event.ON_START) {
                    currentOnStart()
                }
            }

            // Add the observer to the lifecycle
            lifecycleOwner.lifecycle.addObserver(observer)

            // When the effect leaves the Composition, remove the observer
            onDispose {
                lifecycleOwner.lifecycle.removeObserver(observer)
            }
        }
    }
}

internal fun trackScreenView(name: String?) {
    Timber.d("Track screen: $name")
    Firebase.analytics.logEvent(FirebaseAnalytics.Event.SCREEN_VIEW) {
        param(FirebaseAnalytics.Param.SCREEN_NAME, name?.replace(" ", "_") ?: "Unknown")
    }
}

fun trackAction(action: String) {
    Timber.d("Track action: $action")
    Firebase.analytics.logEvent(action.replace(" ", "_")) {
    }
}

fun trackReviewRequested() {
    Timber.d("Track action: Review requested")
    Firebase.analytics.logEvent(ReviewRequested.replace(" ", "_")) {}
}

fun trackPurchaseClick(purchaseId: String) {
    Timber.d("Track action: Purchase: $purchaseId")
    Firebase.analytics.logEvent(PurchaseClick.replace(" ", "_")) {
        param(FirebaseAnalytics.Param.ITEM_ID, purchaseId)
    }
}

fun trackLoadGame(savedGame: SavedGame) {
    Timber.d("Track action: $LoadGame (${savedGame.gameType?.name}: ${savedGame.position})")
    Firebase.analytics.logEvent(LoadGame.replace(" ", "_")) {
        param(FirebaseAnalytics.Param.INDEX, savedGame.position.toDouble())
        param(FirebaseAnalytics.Param.CONTENT_TYPE, savedGame.gameType?.name ?: "Unknown")
    }
}

fun trackDeleteGame(savedGame: SavedGame) {
    Timber.d("Track action: $DeleteGame (${savedGame.gameType?.name}: ${savedGame.position})")
    Firebase.analytics.logEvent(DeleteGame.replace(" ", "_")) {
        param(FirebaseAnalytics.Param.INDEX, savedGame.position.toDouble())
        param(FirebaseAnalytics.Param.CONTENT_TYPE, savedGame.gameType?.name ?: "Unknown")
    }
}

fun trackShuffleGame(gameType: GameType, position: Int) {
    Timber.d("Track action: $ShuffleGame (${gameType.name}: $position)")
    Firebase.analytics.logEvent(ShuffleGame.replace(" ", "_")) {
        param(FirebaseAnalytics.Param.INDEX, position.toDouble())
        param(FirebaseAnalytics.Param.CONTENT_TYPE, gameType.name.replace(" ", "_"))
    }
}

fun trackEndGame(gameType: GameType, position: Int) {
    Timber.d("Track action: $EndGame (${gameType.name}: $position)")
    Firebase.analytics.logEvent(EndGame.replace(" ", "_")) {
        param(FirebaseAnalytics.Param.INDEX, position.toDouble())
        param(FirebaseAnalytics.Param.CONTENT_TYPE, gameType.name.replace(" ", "_"))
    }
}
