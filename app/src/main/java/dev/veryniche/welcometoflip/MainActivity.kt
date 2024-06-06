package dev.veryniche.welcometoflip

import android.os.Bundle
import android.view.Window
import android.view.WindowManager
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.windowInsetsBottomHeight
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.adaptive.ExperimentalMaterial3AdaptiveApi
import androidx.compose.material3.adaptive.currentWindowAdaptiveInfo
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
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
import dev.veryniche.welcometoflip.viewmodels.MainViewModel
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

    private fun setKeepScreenOn(window: Window, screenOn: Boolean){
        if (screenOn) {
            window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
        } else {
            window.clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
        }
        Timber.d("Updating screen on setting to: $screenOn")
    }

    @OptIn(ExperimentalMaterial3AdaptiveApi::class)
    @Composable
    fun WelcomeToFlipApp() {
        val coroutineScope = rememberCoroutineScope()
        val purchaseManager = remember { PurchaseManager(this, coroutineScope) }
        val viewModel: MainViewModel = hiltViewModel<MainViewModel, MainViewModel.MainViewModelFactory> { factory ->
            factory.create(purchaseManager)
        }
        WelcomeToFlipTheme {
            val navController = rememberNavController()
            val snackbarHostState = remember { SnackbarHostState() }
            val showWelcomeDialogOnStart by viewModel.showWelcomeDialog.collectAsStateWithLifecycle(null,
                lifecycleOwner = androidx.compose.ui.platform.LocalLifecycleOwner.current
            )
            var showWelcomeDialog by rememberSaveable(showWelcomeDialogOnStart) {
                mutableStateOf(showWelcomeDialogOnStart)
            }
            val availableInAppProducts by viewModel.availableInAppProducts.collectAsStateWithLifecycle(mapOf(),
                lifecycleOwner = androidx.compose.ui.platform.LocalLifecycleOwner.current
            )
            var showPurchaseErrorMessage by rememberSaveable { mutableStateOf<Int?>(null) }
            val showAds by viewModel.showAds.collectAsStateWithLifecycle(false,
                lifecycleOwner = androidx.compose.ui.platform.LocalLifecycleOwner.current)

            val windowSizeClass: WindowSizeClass = currentWindowAdaptiveInfo().windowSizeClass

            val keepScreenOn by viewModel.keepScreenOn.collectAsStateWithLifecycle(false,
                lifecycleOwner = androidx.compose.ui.platform.LocalLifecycleOwner.current)

            Column(modifier = Modifier.background(MaterialTheme.colorScheme.background)) {
                WelcomeToFlipNavHost(
                    navController = navController,
                    mainViewModel = viewModel,
                    purchaseStatus = availableInAppProducts,
                    snackbarHostState = snackbarHostState,
                    keepScreenOn = keepScreenOn,
                    onKeepScreenOnSet = {
                        viewModel.updateKeepScreenOn(it)
                        setKeepScreenOn(window, it)
                    },
                    keepScreenOnAction = {
                        setKeepScreenOn(window, it)
                    },
                    showPurchaseErrorMessage = {
                        showPurchaseErrorMessage = it
                    },
                    onGameEnd = {
                        viewModel.requestReviewIfAble(this@MainActivity)
                    },
                    onShowInterstitialAd = { location ->
                        if (showAds) {
                            showInterstitialAd(this@MainActivity, location)
                        }
                    },
                    windowSizeClass = windowSizeClass,
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
                    purchaseStatus = availableInAppProducts,
                    onPurchaseClick = { productId ->
                        viewModel.purchaseProduct(productId) {
                            showPurchaseErrorMessage = it
                        }
                    },
                    onDismissRequest = {
                        showWelcomeDialog = false
                    },
                    saveShowWelcomeOnStart = { value ->
                        viewModel.updateShowWelcomeOnStart(!value)
                    },
                    windowSizeClass = windowSizeClass,
                )
            }

            showPurchaseErrorMessage?.let { message ->
                AlertDialog(
                    onDismissRequest = { showPurchaseErrorMessage = null },
                    title = { Text(stringResource(R.string.app_name)) },
                    text = { Text(stringResource(message)) },
                    confirmButton = {
                        TextButton(onClick = {
                            showPurchaseErrorMessage = null
                        }) {
                            Text(stringResource(R.string.purchase_error_dismiss))
                        }
                    }
                )
            }
        }
    }
}
