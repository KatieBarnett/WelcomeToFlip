package dev.veryniche.welcometoflip.components

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionLayout
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import dev.veryniche.welcometoflip.core.models.SoloA
import dev.veryniche.welcometoflip.core.models.WelcomeToClassic
import dev.veryniche.welcometoflip.core.models.WelcomeToTheMoon
import dev.veryniche.welcometoflip.core.models.welcomeToClassicDeck
import dev.veryniche.welcometoflip.util.transformCardsToMap
import dev.veryniche.welcometoflip.viewmodels.SoloGamePhase
import dev.veryniche.welcometoflip.viewmodels.SoloState

val LocalAnimatedVisibilityScope = compositionLocalOf<AnimatedVisibilityScope?> { null }

@OptIn(ExperimentalSharedTransitionApi::class)
val LocalSharedTransitionScope = compositionLocalOf<SharedTransitionScope?> { null }

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun SoloAnimatedLayout(
    state: SoloState,
    modifier: Modifier = Modifier
) {
    val aiCards: @Composable (modifier: Modifier) -> Unit = { modifier ->
        when (state.gameType) {
            WelcomeToClassic -> {
                SoloAAALayout(
                    aaaCards = state.aiStack,
                    effectCards = state.drawnEffectCards,
                    modifier = modifier
                )
            }
            WelcomeToTheMoon -> {
                SoloAstraLayout(
                    astraCards = state.aiStack.transformCardsToMap(),
                    effectCards = state.drawnEffectCards,
                    modifier = modifier
                )
            }
        }
    }

    SharedTransitionLayout {
        CompositionLocalProvider(
            LocalSharedTransitionScope provides this
        ) {
            AnimatedContent(
                targetState = state,
                transitionSpec = {
                    fadeIn(initialAlpha = 1f) togetherWith fadeOut(targetAlpha = 1f)
                },
                label = "cardSharedElementTransition"
            ) { targetResult ->
                CompositionLocalProvider(LocalAnimatedVisibilityScope provides this) {
                    targetResult.run {
                        when (phase) {
                            is SoloGamePhase.AiSelection -> TODO()
                            SoloGamePhase.DrawCards -> {
                                DrawCardsLayout(
                                    drawStack = drawStack.firstOrNull(),
                                    discardStack = discardStack.firstOrNull(),
                                    activeCards = activeCards,
                                    gameType = gameType,
                                    aiCards = aiCards,
                                    modifier = modifier
                                )
                            }
                            is SoloGamePhase.EffectCardDrawn -> TODO()
                            SoloGamePhase.EndGame -> TODO()
                            SoloGamePhase.PlayerSelection -> {
                                PlayerSelectionLayout(
                                    drawStack = drawStack.firstOrNull(),
                                    discardStack = discardStack.firstOrNull(),
                                    activeCards = activeCards,
                                    gameType = gameType,
                                    aiCards = aiCards,
                                    modifier = Modifier.fillMaxSize()
                                )
                            }
                            SoloGamePhase.Reshuffle -> TODO()
                            SoloGamePhase.SelectCards -> {
                                SelectCardsLayout(
                                    drawStack = drawStack.firstOrNull(),
                                    discardStack = discardStack.firstOrNull(),
                                    activeCards = activeCards,
                                    gameType = gameType,
                                    aiCards = aiCards,
                                    modifier = modifier
                                )
                            }
                            SoloGamePhase.Setup -> TODO()
                        }
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun SoloAnimatedLayoutPreview() {
    val states = listOf(
        SoloState(
            phase = SoloGamePhase.DrawCards,
            drawStack = welcomeToClassicDeck.subList(0, 10),
            discardStack = welcomeToClassicDeck.subList(10, 15),
            activeCards = welcomeToClassicDeck.subList(15, 18),
            drawnEffectCards = listOf(SoloA),
            aiStack = welcomeToClassicDeck.subList(18, 33),
            gameType = WelcomeToClassic,
        ),
        SoloState(
            phase = SoloGamePhase.SelectCards,
            drawStack = welcomeToClassicDeck.subList(0, 10),
            discardStack = welcomeToClassicDeck.subList(10, 15),
            activeCards = welcomeToClassicDeck.subList(15, 18),
            drawnEffectCards = listOf(SoloA),
            aiStack = welcomeToClassicDeck.subList(18, 33),
            gameType = WelcomeToClassic,
        ),
        SoloState(
            phase = SoloGamePhase.PlayerSelection,
            drawStack = welcomeToClassicDeck.subList(0, 10),
            discardStack = welcomeToClassicDeck.subList(10, 15),
            activeCards = welcomeToClassicDeck.subList(15, 18),
            drawnEffectCards = listOf(SoloA),
            aiStack = welcomeToClassicDeck.subList(18, 33),
            gameType = WelcomeToClassic,
        )
    )
    var currentIndex by remember { mutableIntStateOf(0) }

    Column(Modifier.fillMaxSize(), horizontalAlignment = Alignment.CenterHorizontally) {
        Button(onClick = {
            currentIndex++
        }) {
            Text("Transition to " + states[(currentIndex + 1) % states.size].phase.name)
        }
        SoloAnimatedLayout(
            state = states[currentIndex % states.size],
            modifier = Modifier.weight(1f)
        )
    }
}
