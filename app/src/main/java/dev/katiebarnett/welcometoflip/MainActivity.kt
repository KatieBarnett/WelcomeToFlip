package dev.katiebarnett.welcometoflip

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.rememberNavController
import com.google.android.material.composethemeadapter.MdcTheme
import dagger.hilt.android.AndroidEntryPoint

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
        MdcTheme {
            val navController = rememberNavController()
            Scaffold(
                topBar = {
                    CenterAlignedTopAppBar(
                        title = { Text(text = stringResource(id = R.string.app_name))}
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