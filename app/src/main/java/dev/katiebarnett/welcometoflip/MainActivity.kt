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
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import dagger.hilt.android.AndroidEntryPoint
import dev.katiebarnett.welcometoflip.theme.WelcomeToFlipTheme

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            WelcomeToFlipApp()
        }
    }
    
    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun WelcomeToFlipApp() {
        WelcomeToFlipTheme {
            val navController = rememberNavController()
            
            var isBackButtonVisible by remember { mutableStateOf(false) }
            DisposableEffect(navController) {
                val listener = NavController.OnDestinationChangedListener { controller, _, _ ->
                    isBackButtonVisible = controller.previousBackStackEntry != null
                }
                navController.addOnDestinationChangedListener(listener)
                onDispose {
                    navController.removeOnDestinationChangedListener(listener)
                }
            }

            Scaffold(
                topBar = {
                    CenterAlignedTopAppBar(
                        title = { Text(text = stringResource(id = R.string.app_name))}, 
                        navigationIcon = { if (isBackButtonVisible) {
                                IconButton(onClick = { navController.navigateUp() }) {
                                    Icon(
                                        imageVector = Icons.Filled.ArrowBack,
                                        contentDescription = stringResource(id = R.string.navigate_back)
                                    )
                                }
                            }
                        }
                    )
                }
            ) { innerPadding ->
                Box(Modifier.padding(innerPadding)) {
                    WelcomeToFlipNavHost(navController = navController)
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