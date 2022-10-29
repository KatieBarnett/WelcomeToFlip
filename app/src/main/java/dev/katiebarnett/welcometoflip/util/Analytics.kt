package dev.katiebarnett.welcometoflip.util

import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.ktx.analytics
import com.google.firebase.analytics.ktx.logEvent
import com.google.firebase.ktx.Firebase
import dev.katiebarnett.welcometoflip.core.models.GameType
import dev.katiebarnett.welcometoflip.core.models.SavedGame
import dev.katiebarnett.welcometoflip.util.Analytics.Action.DeleteGame
import dev.katiebarnett.welcometoflip.util.Analytics.Action.EndGame
import dev.katiebarnett.welcometoflip.util.Analytics.Action.LoadGame
import dev.katiebarnett.welcometoflip.util.Analytics.Action.ShuffleGame

private const val ANALYTICS_LOG_TAG = "Analytics"

object Analytics {
    object Screen {
        const val ChooseGame = "Choose Game"
    }

    object Action {
        const val LoadGame = "Load Game"
        const val DeleteGame = "Delete Game"
        const val ShuffleGame = "Shuffle Game"
        const val EndGame = "End Game"
    }
}

@Composable
fun TrackedScreen(
    lifecycleOwner: LifecycleOwner = LocalLifecycleOwner.current,
    onStart: () -> Unit, // Send the 'started' analytics event
) {
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

internal fun trackScreenView(name: String?) {
    Log.d(ANALYTICS_LOG_TAG, "Track screen: $name")
    Firebase.analytics.logEvent(FirebaseAnalytics.Event.SCREEN_VIEW) {
        param(FirebaseAnalytics.Param.SCREEN_NAME, name ?: "Unknown")
    }
}

fun trackLoadGame(savedGame: SavedGame) {
    Log.d(ANALYTICS_LOG_TAG, "Track action: $LoadGame (${savedGame.gameType?.name}: ${savedGame.position})")
    Firebase.analytics.logEvent(LoadGame) {
        param(FirebaseAnalytics.Param.INDEX, savedGame.position.toDouble())
        param(FirebaseAnalytics.Param.CONTENT_TYPE, savedGame.gameType?.name ?: "Unknown")
    }
}

fun trackDeleteGame(savedGame: SavedGame) {
    Log.d(ANALYTICS_LOG_TAG, "Track action: $DeleteGame (${savedGame.gameType?.name}: ${savedGame.position})")
    Firebase.analytics.logEvent(DeleteGame) {
        param(FirebaseAnalytics.Param.INDEX, savedGame.position.toDouble())
        param(FirebaseAnalytics.Param.CONTENT_TYPE, savedGame.gameType?.name ?: "Unknown")
    }
}

fun trackShuffleGame(gameType: GameType, position: Int) {
    Log.d(ANALYTICS_LOG_TAG, "Track action: $ShuffleGame (${gameType.name}: ${position})")
    Firebase.analytics.logEvent(ShuffleGame) {
        param(FirebaseAnalytics.Param.INDEX, position.toDouble())
        param(FirebaseAnalytics.Param.CONTENT_TYPE, gameType.name)
    }
}

fun trackEndGame(gameType: GameType, position: Int) {
    Log.d(ANALYTICS_LOG_TAG, "Track action: $EndGame (${gameType.name}: ${position})")
    Firebase.analytics.logEvent(EndGame) {
        param(FirebaseAnalytics.Param.INDEX, position.toDouble())
        param(FirebaseAnalytics.Param.CONTENT_TYPE, gameType.name)
    }
}