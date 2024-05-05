package dev.veryniche.welcometoflip

// import com.google.android.gms.ads.MobileAds
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.windowInsetsBottomHeight
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.compose.rememberNavController
import com.google.android.gms.ads.MobileAds
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.ktx.analytics
import com.google.firebase.ktx.Firebase
import dagger.hilt.android.AndroidEntryPoint
import dev.veryniche.welcometoflip.ads.BannerAd
import dev.veryniche.welcometoflip.ads.BannerAdLocation
import dev.veryniche.welcometoflip.ads.showInterstitialAd
import dev.veryniche.welcometoflip.components.WelcomeDialog
import dev.veryniche.welcometoflip.purchase.PurchaseManager
import dev.veryniche.welcometoflip.theme.WelcomeToFlipTheme
import timber.log.Timber

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private lateinit var firebaseAnalytics: FirebaseAnalytics

    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        firebaseAnalytics = Firebase.analytics
        MobileAds.initialize(this) { initializationStatus ->
            Timber.d("AdMob init: ${initializationStatus.adapterStatusMap}")
        }
        setContent {
            WelcomeToFlipApp()
        }
    }

    @Composable
    fun WelcomeToFlipApp() {
        val coroutineScope = rememberCoroutineScope()
        val purchaseManager = remember { PurchaseManager(this, coroutineScope) }
        val viewModel: MainViewModel = hiltViewModel<MainViewModel, MainViewModel.MainViewModelFactory> { factory ->
            factory.create(purchaseManager)
        }
        WelcomeToFlipTheme {
            val navController = rememberNavController()
            val showWelcomeDialogOnStart by viewModel.showWelcomeDialog.collectAsStateWithLifecycle(null)
            var showWelcomeDialog by remember(showWelcomeDialogOnStart) { mutableStateOf(showWelcomeDialogOnStart) }
            val showAds by viewModel.showAds.collectAsStateWithLifecycle(false)
            Column(modifier = Modifier.background(MaterialTheme.colorScheme.background)) {
                WelcomeToFlipNavHost(
                    navController = navController,
                    mainViewModel = viewModel,
                    onGameEnd = {
                        viewModel.requestReviewIfAble(this@MainActivity)
                    },
                    onShowInterstitialAd = { location ->
                        if (showAds) {
                            showInterstitialAd(this@MainActivity, location)
                        }
                    },
                    modifier = Modifier.weight(1f)
                )
                if (showAds) {
                    BannerAd(location = BannerAdLocation.MainScreen)
                }
                Spacer(
                    Modifier.windowInsetsBottomHeight(
                        WindowInsets.systemBars
                    )
                )
            }
            if (showWelcomeDialog == true) {
                WelcomeDialog(
                    navController = navController,
                    onDismissRequest = {
                        showWelcomeDialog = false
                    },
                    saveShowWelcomeOnStart = { value ->
                        viewModel.updateShowWelcomeOnStart(!value)
                    }
                )
            }
        }
    }

    @Preview(group = "Full App", showSystemUi = true, showBackground = true)
    @Composable
    fun DefaultPreview() {
        WelcomeToFlipApp()
    }
}
