package dev.veryniche.welcometoflip.util

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import dev.veryniche.welcometoflip.theme.Dimen.AppBar.CollapsedTextSize
import dev.veryniche.welcometoflip.theme.Dimen.AppBar.ExpandedTextSize

fun getTopAppBarTextSize(collapsedFraction: Float) = (CollapsedTextSize + (ExpandedTextSize - CollapsedTextSize) * (1 - collapsedFraction))



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