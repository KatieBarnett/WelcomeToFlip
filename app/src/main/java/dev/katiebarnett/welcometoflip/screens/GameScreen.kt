@file:OptIn(ExperimentalMaterial3Api::class)

package dev.katiebarnett.welcometoflip.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.google.android.material.composethemeadapter.MdcTheme
import dev.katiebarnett.welcometoflip.GameViewModel
import dev.katiebarnett.welcometoflip.MainViewModel
import dev.katiebarnett.welcometoflip.R
import dev.katiebarnett.welcometoflip.data.GameType

@Composable
fun GameBody(viewModel: GameViewModel,
             gameType: GameType,
             modifier: Modifier = Modifier
) {
    viewModel.initialiseGame(gameType)
    Column(
        modifier
            .fillMaxSize()
            .padding(dimensionResource(id = R.dimen.spacing))
    ) {
        Text(stringResource(id = gameType.name))
    }
}

@Preview(showBackground = true)
@Composable
fun GameScreenPreview() {
    MdcTheme {
//        GameBody(Modifier)
    }
}