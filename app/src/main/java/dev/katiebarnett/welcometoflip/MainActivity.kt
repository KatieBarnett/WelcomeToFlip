package dev.katiebarnett.welcometoflip

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.google.android.material.composethemeadapter.MdcTheme
import dagger.hilt.android.AndroidEntryPoint
import dev.katiebarnett.welcometoflip.data.mapToGameType
import dev.katiebarnett.welcometoflip.screens.ChooseGameBody
import dev.katiebarnett.welcometoflip.screens.GameBody

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
                    NavHost(
                        navController = navController,
                        startDestination = WelcomeToFlipScreen.ChooseGame.name
                    ) {
                        composable(WelcomeToFlipScreen.ChooseGame.name) {
                            ChooseGameBody(chooseGameAction = {
                                navController.navigate(
                                    route = "${WelcomeToFlipScreen.Game.name}/${it.name}")
                            })
                        }
                        composable(
                            "${WelcomeToFlipScreen.Game.name}/{gameType}",
                            arguments = listOf(navArgument("gameType") { type = NavType.IntType })
                            ) {
                            it.arguments?.getInt("gameType")?.mapToGameType()?.let { gameType ->
                                val viewModel = hiltViewModel<GameViewModel>()
                                viewModel.initialiseGame(gameType)
                                GameBody(
                                    viewModel = viewModel,
                                    gameType = gameType
                                )
                            }
                        }
                    }
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