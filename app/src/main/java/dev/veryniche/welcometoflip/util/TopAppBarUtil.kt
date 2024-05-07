package dev.veryniche.welcometoflip.util

import androidx.annotation.StringRes
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.LargeTopAppBar
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MediumTopAppBar
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import dev.veryniche.welcometoflip.components.AboutActionIcon
import dev.veryniche.welcometoflip.components.NavigationIcon
import dev.veryniche.welcometoflip.components.ShopActionIcon
import dev.veryniche.welcometoflip.theme.Dimen.AppBar.CollapsedTextSize
import dev.veryniche.welcometoflip.theme.Dimen.AppBar.ExpandedTextSize

fun getTopAppBarTextSize(collapsedFraction: Float) = (CollapsedTextSize + (ExpandedTextSize - CollapsedTextSize) * (1 - collapsedFraction))

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CollapsingTopAppBar(
    @StringRes titleRes: Int,
    navController: NavController,
    scrollBehavior: TopAppBarScrollBehavior,
    showShopMenuItem: Boolean,
    showAboutMenuItem: Boolean,
    modifier: Modifier = Modifier
) {
    val topAppBarTextSize = getTopAppBarTextSize(scrollBehavior.state.collapsedFraction)

    val fontScale = LocalDensity.current.fontScale

    if (fontScale > 1.5) {
        LargeTopAppBar(
            title = {
                Text(
                    text = stringResource(id = titleRes),
                    overflow = TextOverflow.Ellipsis,
                    fontSize = topAppBarTextSize.sp
                )
            },
            navigationIcon = { NavigationIcon(navController = navController) },
            actions = {
                if (showShopMenuItem) {
                    ShopActionIcon(navController = navController)
                }
                if (showAboutMenuItem) {
                    AboutActionIcon(navController = navController)
                }
            },
            scrollBehavior = scrollBehavior,
            colors = getLargeTopAppBarColors(),
            modifier = modifier
        )
    } else {
        MediumTopAppBar(
            title = {
                Text(
                    text = fontScale.toString(),
//                        text = stringResource(titleRes),
                    fontSize = topAppBarTextSize.sp
                )
            },
            navigationIcon = { NavigationIcon(navController = navController) },
            actions = {
                if (showShopMenuItem) {
                    ShopActionIcon(navController = navController)
                }
                AboutActionIcon(navController)
            },
            scrollBehavior = scrollBehavior,
            colors = getMediumTopAppBarColors(),
            modifier = modifier
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun getLargeTopAppBarColors() = TopAppBarDefaults.largeTopAppBarColors(
    containerColor = MaterialTheme.colorScheme.primary,
    scrolledContainerColor = MaterialTheme.colorScheme.primary,
    navigationIconContentColor = MaterialTheme.colorScheme.onPrimary,
    titleContentColor = MaterialTheme.colorScheme.onPrimary,
    actionIconContentColor = MaterialTheme.colorScheme.onPrimary,
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun getMediumTopAppBarColors() = TopAppBarDefaults.mediumTopAppBarColors(
    containerColor = MaterialTheme.colorScheme.primary,
    scrolledContainerColor = MaterialTheme.colorScheme.primary,
    navigationIconContentColor = MaterialTheme.colorScheme.onPrimary,
    titleContentColor = MaterialTheme.colorScheme.onPrimary,
    actionIconContentColor = MaterialTheme.colorScheme.onPrimary,
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun getTopAppBarColors() = TopAppBarDefaults.topAppBarColors(
    containerColor = MaterialTheme.colorScheme.primary,
    scrolledContainerColor = MaterialTheme.colorScheme.primary,
    navigationIconContentColor = MaterialTheme.colorScheme.onPrimary,
    titleContentColor = MaterialTheme.colorScheme.onPrimary,
    actionIconContentColor = MaterialTheme.colorScheme.onPrimary,
)
