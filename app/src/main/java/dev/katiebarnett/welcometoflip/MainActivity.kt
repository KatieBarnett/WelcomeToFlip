package dev.katiebarnett.welcometoflip

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.twotone.Refresh
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import com.google.android.material.composethemeadapter.MdcTheme
import dagger.hilt.android.AndroidEntryPoint
import dev.katiebarnett.welcometoflip.data.GameType

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

//    private val viewModel: MainViewModel by viewModels()
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MdcTheme {
                GameList()
            }
        }
    }

    @Composable
    fun GameList() {

        val viewModel: MainViewModel = viewModel()
        Column {
            viewModel.games.forEach {
                Game(it)
            }
        }
    }
    
    @Composable
    fun Game(gameType: GameType) {
        Column {
            Icon(painter = painterResource(id = gameType.icon), contentDescription = stringResource(id = gameType.name))
            Text(text = stringResource(gameType.name))
        }
    }

    @Preview(showSystemUi = true, showBackground = true)
    @Composable
    fun DefaultPreview() {
        MdcTheme {
            GameList()
        }
    }
}