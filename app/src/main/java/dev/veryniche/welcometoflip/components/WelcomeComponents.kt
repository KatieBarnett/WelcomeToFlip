package dev.veryniche.welcometoflip.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Checkbox
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import dev.veryniche.welcometoflip.About
import dev.veryniche.welcometoflip.R
import dev.veryniche.welcometoflip.theme.Dimen
import dev.veryniche.welcometoflip.theme.WelcomeToFlipTheme
import dev.veryniche.welcometoflip.util.AboutAppText
import dev.veryniche.welcometoflip.util.RemoveAdsText
import dev.veryniche.welcometoflip.util.UnorderedListText

@Composable
fun WelcomeDialog(navController: NavController, onDismissRequest: () -> Unit) {
    AnimatedTransitionDialog(onDismissRequest = onDismissRequest) { dialogHelper ->
        WelcomeDialogContent(navController, onDismissRequest)
    }
}

@Composable
fun WelcomeDialogContent(navController: NavController, onDismissRequest: () -> Unit) {
    val scrollableState = rememberScrollState()
    val checkedState = remember { mutableStateOf(false) }
    Surface(
        shape = RoundedCornerShape(Dimen.Dialog.radius),
        color = MaterialTheme.colorScheme.surface,
        modifier = Modifier.padding(Dimen.spacing)
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(Dimen.spacingDouble),
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .padding(Dimen.spacing)
                .verticalScroll(scrollableState)
        ) {
            Text(
                text = stringResource(id = R.string.welcome_title),
                style = MaterialTheme.typography.headlineLarge,
                color = MaterialTheme.colorScheme.primary,
                fontWeight = FontWeight.Bold
            )
            AboutAppText()
            Text(
                text = stringResource(id = R.string.welcome_coming_soon_title),
                style = MaterialTheme.typography.headlineMedium,
                color = MaterialTheme.colorScheme.primary,
                fontWeight = FontWeight.Bold
            )
            UnorderedListText(
                textLines = listOf(
                    R.string.welcome_coming_soon_ul_1
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = Dimen.spacing)
            )
            RemoveAdsText({
                navController.navigate(About.route)
                onDismissRequest.invoke(/*checkedState.value*/)
            })
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = stringResource(id = R.string.welcome_dont_show_again)
                )
                Checkbox(
                    checked = checkedState.value,
                    onCheckedChange = { checkedState.value = it }
                )
            }
            ThemedButton(content = {
                Text(stringResource(id = R.string.welcome_close))
            }, onClick = {
                onDismissRequest.invoke(/*checkedState.value*/)
            })
        }
    }
}

@Preview(group = "Welcome Dialog", showBackground = true)
@Composable
fun WelcomeDialogPreview() {
    WelcomeToFlipTheme {
        WelcomeDialogContent(rememberNavController(), {})
    }
}