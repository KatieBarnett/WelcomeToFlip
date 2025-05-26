package dev.veryniche.welcometoflip.components

import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import dev.veryniche.welcometoflip.core.models.Card
import dev.veryniche.welcometoflip.core.models.SoloA
import dev.veryniche.welcometoflip.core.models.WelcomeToClassic
import dev.veryniche.welcometoflip.core.models.WelcomeToTheMoon
import dev.veryniche.welcometoflip.core.models.welcomeToClassicDeck
import dev.veryniche.welcometoflip.core.models.welcomeToTheMoonDeck
import dev.veryniche.welcometoflip.theme.Dimen
import dev.veryniche.welcometoflip.theme.WelcomeToFlipTheme
import dev.veryniche.welcometoflip.util.transformCardsToMap
import dev.veryniche.welcometoflip.viewmodels.SoloState

@Composable
fun SoloGame(
    state: SoloState,
    onDrawCards: () -> Unit,
    onSelectPlayerCard: (Card) -> Unit,
    onSelectAiCard: (Card) -> Unit,
    modifier: Modifier = Modifier,
) {
    SoloSlotLayout(
        drawStack = state.drawStackTopCard,
        discardStack = state.discardStackTopCard,
        activeCards = state.activeCards,
        aiCards = {
            when (state.gameType) {
                WelcomeToClassic -> {
                    SoloAAALayout(
                        aaaCards = state.aiStack,
                        effectCards = state.drawnEffectCards,
                        modifier = it
                    )
                }
                WelcomeToTheMoon -> {
                    SoloAstraLayout(
                        astraCards = state.aiStack.transformCardsToMap(),
                        effectCards = state.drawnEffectCards,
                        modifier = it
                    )
                }
            }
        },
        onDrawCards = onDrawCards,
        onSelectAiCard = onSelectAiCard,
        gameType = state.gameType,
        modifier = modifier.padding(Dimen.spacing)
    )
}

@Preview(group = "Solo Game Screen", showBackground = true)
@Composable
fun SoloGameWelcomeToClassicPreview() {
    WelcomeToFlipTheme {
        SoloGame(
            SoloState(
                drawStack = welcomeToClassicDeck.subList(0, 10),
                discardStack = welcomeToClassicDeck.subList(10, 15),
                activeCards = welcomeToClassicDeck.subList(15, 18),
                drawnEffectCards = listOf(SoloA),
                aiStack = welcomeToClassicDeck.subList(18, 33),
                gameType = WelcomeToClassic,
            ),
            onDrawCards = { },
            onSelectAiCard = { },
            onSelectPlayerCard = {  },
        )
    }
}

@Preview(group = "Solo Game Screen", showBackground = true)
@Composable
fun SoloGameWelcomeToTheMoonPreview() {
    WelcomeToFlipTheme {
        SoloGame(
            SoloState(
                drawStack = welcomeToTheMoonDeck.subList(0, 10),
                discardStack = welcomeToTheMoonDeck.subList(10, 15),
                activeCards = welcomeToTheMoonDeck.subList(15, 18),
                drawnEffectCards = listOf(SoloA),
                aiStack = welcomeToTheMoonDeck.subList(18, 33),
                gameType = WelcomeToTheMoon,
            ),
            onDrawCards = { },
            onSelectAiCard = { },
            onSelectPlayerCard = {  },
        )
    }
}
