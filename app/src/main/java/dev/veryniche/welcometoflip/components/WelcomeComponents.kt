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
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.window.DialogProperties
import dev.veryniche.welcometoflip.R
import dev.veryniche.welcometoflip.purchase.InAppProduct
import dev.veryniche.welcometoflip.purchase.Products
import dev.veryniche.welcometoflip.theme.Dimen
import dev.veryniche.welcometoflip.theme.WelcomeToFlipTheme
import dev.veryniche.welcometoflip.util.AboutAppText
import dev.veryniche.welcometoflip.util.RemoveAdsText
import dev.veryniche.welcometoflip.util.UnorderedListText

@Composable
fun WelcomeDialog(
    purchaseStatus: Map<String, InAppProduct>,
    onPurchaseClick: (String) -> Unit,
    onDismissRequest: () -> Unit,
    saveShowWelcomeOnStart: (Boolean) -> Unit,
    windowSizeClass: WindowSizeClass
) {
    AnimatedTransitionDialog(
        onDismissRequest = onDismissRequest,
        dialogProperties = DialogProperties(usePlatformDefaultWidth = windowSizeClass.widthSizeClass != WindowWidthSizeClass.Compact)
    ) { dialogHelper ->
        WelcomeDialogContent(
            purchaseStatus = purchaseStatus,
            onPurchaseClick = onPurchaseClick,
            saveWelcomePreference = saveShowWelcomeOnStart,
            triggerAnimatedDismiss = dialogHelper::triggerAnimatedDismiss
        )
    }
}

@Composable
fun WelcomeDialogContent(
    purchaseStatus: Map<String, InAppProduct>,
    onPurchaseClick: (String) -> Unit,
    saveWelcomePreference: (Boolean) -> Unit,
    triggerAnimatedDismiss: () -> Unit
) {
    val scrollableState = rememberScrollState()
    var checkedState by remember { mutableStateOf(false) }
    Surface(
        shape = RoundedCornerShape(Dimen.Dialog.radius),
        color = MaterialTheme.colorScheme.surface,
        modifier = Modifier.padding(Dimen.spacing)
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(Dimen.spacingDouble),
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .padding(Dimen.spacingDouble)
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
            purchaseStatus[Products.adRemoval]?.let {
                if (it.purchased != true && purchaseStatus[Products.bundle]?.purchased != true) {
                    RemoveAdsText({
                        onPurchaseClick.invoke(it.productId)
                    })
                }
            }
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = stringResource(id = R.string.welcome_dont_show_again)
                )
                Checkbox(
                    checked = checkedState,
                    onCheckedChange = { checkedState = it }
                )
            }
            DialogButton(
                textRes = R.string.welcome_close,
                onClick = {
                    saveWelcomePreference.invoke(checkedState)
                    triggerAnimatedDismiss.invoke()
                }
            )
        }
    }
}

@Preview(group = "Welcome Dialog", showBackground = true)
@Composable
fun WelcomeDialogPreview() {
    WelcomeToFlipTheme {
        WelcomeDialogContent(mapOf(), {}, {}, {})
    }
}
