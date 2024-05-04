package dev.veryniche.welcometoflip

// import com.google.android.gms.ads.MobileAds
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.compose.rememberNavController
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.ktx.analytics
import com.google.firebase.ktx.Firebase
import dagger.hilt.android.AndroidEntryPoint
import dev.veryniche.welcometoflip.components.WelcomeDialog
import dev.veryniche.welcometoflip.purchase.PurchaseManager
import dev.veryniche.welcometoflip.purchase.PurchaseStatus
import dev.veryniche.welcometoflip.theme.WelcomeToFlipTheme

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private lateinit var firebaseAnalytics: FirebaseAnalytics

    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        firebaseAnalytics = Firebase.analytics
//        MobileAds.initialize(this) { initializationStatus ->
//            Timber.d("AdMob init: ${initializationStatus.adapterStatusMap}")
//        }
        setContent {
            val coroutineScope = rememberCoroutineScope()
            val purchaseManager = remember { PurchaseManager(this, coroutineScope) }
            LaunchedEffect(Unit) {
                purchaseManager.connectToBilling()
            }

            val purchasedProducts by purchaseManager.purchases.collectAsStateWithLifecycle()
            var showPurchaseErrorMessage by rememberSaveable { mutableStateOf<Int?>(null) }
            WelcomeToFlipApp(purchasedProducts)
        }
    }

    @Composable
    fun WelcomeToFlipApp(purchasedProducts: List<String>) {
        val viewModel: MainViewModel = hiltViewModel<MainViewModel, MainViewModel.MainViewModelFactory> { factory ->
            factory.create(purchasedProducts)
        }
        WelcomeToFlipTheme {
            val navController = rememberNavController()
            val showWelcomeDialogOnStart by viewModel.showWelcomeDialog.collectAsStateWithLifecycle(null)
            var showWelcomeDialog by remember(showWelcomeDialogOnStart) { mutableStateOf(showWelcomeDialogOnStart) }
            var purchaseStatus by remember { mutableStateOf<Map<String, PurchaseStatus>>(mapOf()) }

            WelcomeToFlipNavHost(
                navController = navController,
                purchaseStatus = purchaseStatus,
                onPurchaseClick = {
                    // TODO
                },
                mainViewModel = viewModel,
                onGameEnd = {
                    viewModel.requestReviewIfAble(this)
                }
            )
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
        WelcomeToFlipApp(listOf())
    }
}
