package dev.katiebarnett.welcometoflip

import androidx.activity.ComponentActivity
import androidx.activity.viewModels
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class GameActivity : ComponentActivity() {

    private val viewModel: GameViewModel by viewModels()
    
}