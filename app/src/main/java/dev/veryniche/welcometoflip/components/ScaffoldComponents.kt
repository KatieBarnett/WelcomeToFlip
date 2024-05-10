package dev.veryniche.welcometoflip.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.BrightnessHigh
import androidx.compose.material.icons.filled.BrightnessLow
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavController
import dev.veryniche.welcometoflip.About
import dev.veryniche.welcometoflip.R
import dev.veryniche.welcometoflip.Shop
import kotlinx.coroutines.launch

@Composable
fun NavigationIcon(navController: NavController) {
    if (navController.previousBackStackEntry != null) {
        IconButton(
            onClick = { navController.navigateUp() }
        ) {
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
        onClick = { navController.navigate(About.route) }
    ) {
        Icon(
            imageVector = Icons.Filled.Info,
            contentDescription = stringResource(id = R.string.navigate_about)
        )
    }
}

@Composable
fun ShopActionIcon(navController: NavController) {
    IconButton(
        onClick = { navController.navigate(Shop.route) }
    ) {
        Icon(
            imageVector = Icons.Filled.ShoppingCart,
            contentDescription = stringResource(id = R.string.navigate_shop)
        )
    }
}

@Composable
fun ScreenOnToggle(
    keepScreenOn: Boolean,
    onKeepScreenOnSet: (Boolean) -> Unit,
    snackbarHostState: SnackbarHostState,
    modifier: Modifier = Modifier,
) {
    val coroutineScope = rememberCoroutineScope()
    val messageOnToggle = stringResource(
        id = if (!keepScreenOn) {
            R.string.toggle_screen_on_confirmation
        } else {
            R.string.toggle_screen_off_confirmation
        }
    )
    IconButton(
        modifier = modifier,
        onClick = {
            onKeepScreenOnSet.invoke(!keepScreenOn)
            coroutineScope.launch {
                snackbarHostState.showSnackbar(messageOnToggle)
            }
        }
    ) {
        Icon(
            imageVector = if (keepScreenOn) {
                Icons.Filled.BrightnessHigh
            } else {
                Icons.Filled.BrightnessLow
            },
            contentDescription = stringResource(id = R.string.toggle_screen_on)
        )
    }
}
