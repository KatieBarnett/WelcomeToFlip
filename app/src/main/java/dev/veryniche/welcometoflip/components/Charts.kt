package dev.veryniche.welcometoflip.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.unit.dp
import dev.veryniche.welcometoflip.core.models.Action
import dev.veryniche.welcometoflip.core.models.Card
import dev.veryniche.welcometoflip.core.models.Number
import dev.veryniche.welcometoflip.core.models.allNumbers
import dev.veryniche.welcometoflip.core.models.welcomeToClassicDeck
import dev.veryniche.welcometoflip.core.models.welcomeToTheMoonDeck
import dev.veryniche.welcometoflip.theme.Dimen
import dev.veryniche.welcometoflip.theme.WelcomeToFlipTheme
import timber.log.Timber

@Composable
fun DeckChart(deck: List<Card>, modifier: Modifier = Modifier) {
    BoxWithConstraints(modifier = modifier, contentAlignment = Alignment.TopStart) {
        val heightRemovingPadding = maxHeight - Dimen.spacingDouble * 2 - Dimen.spacing
        val widthRemovingPadding = maxWidth - Dimen.spacingDouble * 2 - Dimen.spacing
        val numberGroups = deck.groupBy { it.number }
        val columnCount = numberGroups.maxOf { it.value.size } + 1
        var itemSize = widthRemovingPadding / columnCount
        Timber.d("maxHeight: $heightRemovingPadding itemSize: $itemSize")
        while (itemSize * numberGroups.size > heightRemovingPadding) {
            itemSize *= 0.9f
        }
        Timber.d("new: maxHeight: $heightRemovingPadding itemSize: $itemSize")
        Surface(Modifier.padding(Dimen.spacingDouble)) {
            Column(Modifier.padding(top = Dimen.spacing, bottom = Dimen.spacing, end = Dimen.spacing)) {
                allNumbers.forEach { numberItem ->
                    Row(Modifier) {
                        ChartRowNumberElement(
                            numberItem,
                            Modifier
                                .size(itemSize)
                        )
                        deck.filter { it.number == numberItem }.sortedBy { it.action.backgroundColor.hashCode() }
                            .forEach {
                                ChartRowElement(
                                    it.action as Action,
                                    Modifier
                                        .size(itemSize)
                                )
                            }
                    }
                }
            }
        }
    }
}

@Composable
fun ChartRowNumberElement(number: Number, modifier: Modifier = Modifier) {
    Box(modifier = modifier) {
        Image(
            painter = painterResource(number.drawableRes),
            contentDescription = null,
            modifier = modifier
                .padding(4.dp)
                .fillMaxSize()
                .align(Alignment.Center)
        )
    }
}

@Composable
fun ChartRowElement(action: Action, modifier: Modifier = Modifier) {
    action.backgroundColor?.let {
        Box(modifier = modifier.background(it)) {
            Image(
                painter = painterResource(action.drawableRes),
                contentDescription = null,
                modifier = modifier
                    .padding(4.dp)
                    .fillMaxSize()
                    .align(Alignment.Center)
            )
        }
    }
}

class DeckChartPreviewParameterProvider : PreviewParameterProvider<List<Card>> {
    override val values: Sequence<List<Card>> = sequenceOf(
        welcomeToClassicDeck,
        welcomeToTheMoonDeck
    )
}

@Preview(showBackground = true)
@Composable
fun DeckChartPreview(
    @PreviewParameter(DeckChartPreviewParameterProvider::class) data: List<Card>
) {
    WelcomeToFlipTheme {
        DeckChart(data)
    }
}
