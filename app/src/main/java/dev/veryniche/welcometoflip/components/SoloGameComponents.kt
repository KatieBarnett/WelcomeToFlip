package dev.veryniche.welcometoflip.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import dev.veryniche.welcometoflip.R
import dev.veryniche.welcometoflip.core.models.Action
import dev.veryniche.welcometoflip.core.models.Astronaut
import dev.veryniche.welcometoflip.core.models.Card
import dev.veryniche.welcometoflip.core.models.Letter
import dev.veryniche.welcometoflip.core.models.Lightning
import dev.veryniche.welcometoflip.core.models.Number15
import dev.veryniche.welcometoflip.core.models.Park
import dev.veryniche.welcometoflip.core.models.Plant
import dev.veryniche.welcometoflip.core.models.Robot
import dev.veryniche.welcometoflip.core.models.SoloA
import dev.veryniche.welcometoflip.core.models.SoloB
import dev.veryniche.welcometoflip.core.models.SoloC
import dev.veryniche.welcometoflip.core.models.Water
import dev.veryniche.welcometoflip.core.models.X
import dev.veryniche.welcometoflip.core.models.welcomeToClassicDeck
import dev.veryniche.welcometoflip.theme.Dimen
import dev.veryniche.welcometoflip.theme.WelcomeToFlipTheme
import kotlin.collections.chunked

@Composable
fun SoloAAALayout(
    aaaCards: List<Card>,
    effectCards: List<Letter>,
    modifier: Modifier = Modifier
) {
    var cardsInRow by remember { mutableStateOf(13) }
    while (aaaCards.size / 3f > cardsInRow) {
        cardsInRow = cardsInRow + 4
    }
    val astraCardChunks = aaaCards.chunked(cardsInRow)
    Column(verticalArrangement = Arrangement.spacedBy(Dimen.spacing), modifier = modifier) {
        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(Dimen.spacingHalf)
        ) {
            for (j in 0..2) {
                astraCardChunks.getOrNull(j)?.let {
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(Dimen.spacingHalf),
                        modifier = Modifier.weight(1f)
                    ) {
                        for (i in 0..cardsInRow - 1) {
                            it.getOrNull(i)?.let {
                                SoloAAAItem(it, modifier = Modifier.weight(1f))
                            } ?: Spacer(Modifier.weight(1f))
                        }
                    }
                } ?: Spacer(Modifier.weight(1f))
            }
        }
        Row(horizontalArrangement = Arrangement.spacedBy(Dimen.spacing), modifier = Modifier.weight(0.25f)) {
            effectCards.forEach {
                SoloEffectItem(it)
            }
        }
    }
}

@Composable
fun SoloAstraLayout(
    astraCards: Map<Action, Int>,
    effectCards: List<Letter>,
    modifier: Modifier = Modifier
) {
    val astraCardChunks = astraCards.toList().chunked(2)
    Column(verticalArrangement = Arrangement.spacedBy(Dimen.spacing), modifier = modifier) {
        for (i in 0..maxOf(astraCardChunks.size - 1, effectCards.size - 1)) {
            Row(modifier = Modifier.weight(1f)) {
                SoloAstraItem(astraCardChunks.getOrNull(i)?.getOrNull(0), modifier = Modifier.weight(2f))
                SoloAstraItem(astraCardChunks.getOrNull(i)?.getOrNull(1), modifier = Modifier.weight(2f))
                SoloEffectItem(effectCards.getOrNull(i), modifier = Modifier.weight(1f, fill = false))
            }
        }
    }
}

@Composable
fun SoloAstraItem(
    item: Pair<Action, Int>?,
    modifier: Modifier = Modifier
) {
    if (item != null) {
        Row(verticalAlignment = Alignment.CenterVertically, modifier = modifier) {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .weight(1f, fill = false)
                    .aspectRatio(1f)
            ) {
                Surface(
                    color = item.first.backgroundColor ?: MaterialTheme.colorScheme.surface,
                    modifier = Modifier
                        .fillMaxSize()
                        .clip(RoundedCornerShape(Dimen.Card.radius))
                        .border(
                            width = Dimen.Card.border,
                            color = MaterialTheme.colorScheme.outline,
                            shape = RoundedCornerShape(Dimen.Card.radius)
                        )
                ) {}
                Image(
                    painter = painterResource(item.first.drawableRes),
                    contentDescription = stringResource(id = R.string.action_deck_alt),
                    modifier = Modifier
                        .fillMaxSize(0.8f)
                )
            }
            // TODO auto resizing text
            Text(
                text = item.second.toString(),
                fontSize = Dimen.Solo.AstraCardsText.textSize,
                fontWeight = FontWeight.Bold,
                modifier = Modifier
                    .weight(1f)
                    .padding(Dimen.spacingHalf)
            )
        }
    } else {
        Spacer(modifier = modifier)
    }
}

@Composable
fun SoloAAAItem(
    item: Card,
    modifier: Modifier = Modifier
) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier.fillMaxSize()
    ) {
        Surface(
            color = item.action.backgroundColor ?: MaterialTheme.colorScheme.surface,
            modifier = Modifier
                .fillMaxSize()
                .clip(RoundedCornerShape(Dimen.Card.radius))
                .border(
                    width = Dimen.Card.border,
                    color = MaterialTheme.colorScheme.outline,
                    shape = RoundedCornerShape(Dimen.Card.radius)
                )
        ) {}

        Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.fillMaxSize()) {
            Image(
                painter = painterResource(item.number.drawableRes),
                contentDescription = stringResource(id = R.string.action_deck_alt),
                modifier = Modifier
                    .fillMaxSize(0.8f)
                    .weight(1f)
            )
            Image(
                painter = painterResource(item.action.drawableRes),
                contentDescription = stringResource(id = R.string.action_deck_alt),
                modifier = Modifier
                    .padding(Dimen.spacingQuarter)
                    .fillMaxSize(0.8f)
                    .weight(1f)
            )
        }
    }
}

@Composable
fun SoloEffectItem(
    item: Letter?,
    modifier: Modifier = Modifier
) {
    if (item != null) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = modifier
                .aspectRatio(1f)
        ) {
            Surface(
                color = item.backgroundColor ?: MaterialTheme.colorScheme.surface,
                modifier = Modifier
                    .fillMaxSize()
                    .clip(RoundedCornerShape(Dimen.Card.radius))
                    .border(
                        width = Dimen.Card.border,
                        color = MaterialTheme.colorScheme.outline,
                        shape = RoundedCornerShape(Dimen.Card.radius)
                    )
            ) {}
            Image(
                painter = painterResource(item.drawableRes),
                contentDescription = stringResource(id = R.string.action_deck_alt),
                modifier = Modifier
                    .fillMaxSize(0.8f)
            )
        }
    } else {
        Spacer(modifier = modifier)
    }
}

@Preview(group = "Solo Game Components", showBackground = true)
@Composable
fun SoloEffectItemPreview() {
    WelcomeToFlipTheme {
        SoloEffectItem(
            SoloB,
            modifier = Modifier
                .height(100.dp)
                .padding(Dimen.spacing)
        )
    }
}

@Preview(group = "Solo Game Components", showBackground = true)
@Composable
fun SoloAstraItemPreview() {
    WelcomeToFlipTheme {
        SoloAstraItem(
            Plant to 88,
            modifier = Modifier
                .height(100.dp)
                .padding(Dimen.spacing)
        )
    }
}

@Preview(group = "Solo Game Components", showBackground = true)
@Composable
fun SoloAAAItemPreview() {
    WelcomeToFlipTheme {
        SoloAAAItem(
            Card(Park, Number15),
            modifier = Modifier
                .height(100.dp)
                .width(50.dp)
                .padding(Dimen.spacing)
        )
    }
}

@Preview(group = "Solo Game Components", showBackground = true)
@Composable
fun SoloAAALayoutPreviewSmall() {
    WelcomeToFlipTheme {
        SoloAAALayout(
            aaaCards = welcomeToClassicDeck.subList(0, 8),
            effectCards = listOf(SoloA, SoloB, SoloC),
            modifier = Modifier
                .height(200.dp)
                .padding(Dimen.spacing)
        )
    }
}

@Preview(group = "Solo Game Components", showBackground = true)
@Composable
fun SoloAAALayoutPreview() {
    WelcomeToFlipTheme {
        SoloAAALayout(
            aaaCards = welcomeToClassicDeck.subList(0, 33),
            effectCards = listOf(SoloA, SoloB, SoloC),
            modifier = Modifier
                .height(200.dp)
                .padding(Dimen.spacing)
        )
    }
}

@Preview(group = "Solo Game Components", showBackground = true)
@Composable
fun SoloAAALayoutPreviewLarge() {
    WelcomeToFlipTheme {
        SoloAAALayout(
            aaaCards = welcomeToClassicDeck,
            effectCards = listOf(SoloA, SoloB, SoloC),
            modifier = Modifier
                .height(200.dp)
                .padding(Dimen.spacing)
        )
    }
}

@Preview(group = "Solo Game Components", showBackground = true)
@Composable
fun SoloAstraLayoutPreview() {
    WelcomeToFlipTheme {
        SoloAstraLayout(
            astraCards = mapOf(
                Plant to 0,
                Water to 1,
                Lightning to 2,
                Robot to 3,
                Astronaut to 4,
                X to 5
            ),
            effectCards = listOf(SoloA, SoloB, SoloC),
            modifier = Modifier
                .height(200.dp)
                .padding(Dimen.spacing)
        )
    }
}
