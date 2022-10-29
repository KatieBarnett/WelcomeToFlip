package dev.katiebarnett.welcometoflip.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavController
import dev.katiebarnett.welcometoflip.About
import dev.katiebarnett.welcometoflip.R


@Composable
fun NavigationIcon(navController: NavController) {
    if (navController.previousBackStackEntry != null) {
        IconButton(
            onClick = { navController.navigateUp() }) {
            Icon(
                imageVector = Icons.Filled.ArrowBack,
                contentDescription = stringResource(id = R.string.navigate_back)
            )
        }
    }
}

@Composable
fun AboutActionIcon(navController: NavController) {
    IconButton(
        onClick = { navController.navigate(About.route) }) {
        Icon(
            imageVector = Icons.Filled.Info,
            contentDescription = stringResource(id = R.string.navigate_about)
        )
    }
}