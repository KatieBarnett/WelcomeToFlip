package dev.katiebarnett.welcometoflip

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import dagger.hilt.android.AndroidEntryPoint
import dev.katiebarnett.welcometoflip.components.EndGameDialog
import dev.katiebarnett.welcometoflip.components.WelcomeDialog
import dev.katiebarnett.welcometoflip.theme.WelcomeToFlipTheme

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            WelcomeToFlipApp()
        }
    }
    
    @Composable
    fun WelcomeToFlipApp() {
        WelcomeToFlipTheme {
            val navController = rememberNavController()
            val viewModel: MainViewModel = hiltViewModel()
            var showWelcomeDialog by remember { mutableStateOf(true) }
            WelcomeToFlipNavHost(navController = navController)
            if (showWelcomeDialog) {
                WelcomeDialog(navController) {
                    showWelcomeDialog = false
                }
            }
        }
    }

    @Preview(showSystemUi = true, showBackground = true)
    @Composable
    fun DefaultPreview() {
        WelcomeToFlipApp()
    }
}