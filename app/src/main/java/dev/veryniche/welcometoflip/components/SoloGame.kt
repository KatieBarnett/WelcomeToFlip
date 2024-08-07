package dev.veryniche.welcometoflip.components

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import dev.veryniche.welcometoflip.core.models.Astronaut
import dev.veryniche.welcometoflip.core.models.Card
import dev.veryniche.welcometoflip.core.models.Lightning
import dev.veryniche.welcometoflip.core.models.Number1
import dev.veryniche.welcometoflip.core.models.Number2
import dev.veryniche.welcometoflip.core.models.Number3
import dev.veryniche.welcometoflip.core.models.Plant
import dev.veryniche.welcometoflip.core.models.Robot
import dev.veryniche.welcometoflip.core.models.Water
import dev.veryniche.welcometoflip.core.models.X
import dev.veryniche.welcometoflip.viewmodels.SoloState

@Composable
fun SoloGame(
    state: SoloState,
    onDrawCards: () -> Unit,
    onSelectAstraCard: (Card) -> Unit,
    modifier: Modifier = Modifier
) {
    SoloSlotLayout(
        drawStack = state.drawStackTopCard,
        discardStack = state.discardStackTopCard,
        activeCards = state.activeCards,
        astraCards = {
            SoloAstraLayout(
                astraCards = state.astraCards,
                effectCards = state.effectCards,
                modifier = it
            )
        },
        onDrawCards = onDrawCards,
        onSelectAstraCard = onSelectAstraCard,
        modifier = modifier
    )
}

@Preview(group = "Solo Game Screen", showBackground = true)
@Composable
fun SoloGamePreview() {
    val stacks = listOf(
        listOf(
            Card(Robot, Number1),
            Card(Lightning, Number1)
        ),
        listOf(
            Card(Lightning, Number2),
            Card(Plant, Number2)
        ),
        listOf(
            Card(X, Number3),
            Card(Astronaut, Number3),
            Card(Water, Number3)
        )
    )

//    WelcomeToFlipTheme {
//        SoloGame(SoloState(
//            drawStackTopCard = Card(action = Astronaut, number = Number12),
//            discardStackTopCard = Card(action = Lightning, number = Number10),
//            activeCards = listOf(
//                Card(action = X, number = Number1),
//                Card(action = Plant, number = Number2),
//                Card(action = Water, number = Number3)),
//            astraCards = mapOf(
//                Plant to 0,
//                Water to 1,
//                Lightning to 2,
//                Robot to 3,
//                Astronaut to 4,
//                X to 5),
//            effectCards = listOf(AstraA, AstraB, AstraC)
//        ),
//            astraCardAnimationComplete = {})
//    }
}
