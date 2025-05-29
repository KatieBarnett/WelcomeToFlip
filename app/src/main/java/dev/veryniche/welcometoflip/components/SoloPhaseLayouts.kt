package dev.veryniche.welcometoflip.components

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionLayout
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.unit.dp
import dev.veryniche.welcometoflip.R
import dev.veryniche.welcometoflip.core.models.Astronaut
import dev.veryniche.welcometoflip.core.models.Card
import dev.veryniche.welcometoflip.core.models.GameType
import dev.veryniche.welcometoflip.core.models.Lightning
import dev.veryniche.welcometoflip.core.models.Number1
import dev.veryniche.welcometoflip.core.models.Number10
import dev.veryniche.welcometoflip.core.models.Number12
import dev.veryniche.welcometoflip.core.models.Number2
import dev.veryniche.welcometoflip.core.models.Number3
import dev.veryniche.welcometoflip.core.models.Plant
import dev.veryniche.welcometoflip.core.models.Robot
import dev.veryniche.welcometoflip.core.models.SoloA
import dev.veryniche.welcometoflip.core.models.SoloB
import dev.veryniche.welcometoflip.core.models.SoloC
import dev.veryniche.welcometoflip.core.models.Water
import dev.veryniche.welcometoflip.core.models.WelcomeToClassic
import dev.veryniche.welcometoflip.core.models.WelcomeToTheMoon
import dev.veryniche.welcometoflip.core.models.X
import dev.veryniche.welcometoflip.theme.Dimen
import dev.veryniche.welcometoflip.theme.WelcomeToFlipTheme
import dev.veryniche.welcometoflip.util.conditional
import dev.veryniche.welcometoflip.viewmodels.SoloGamePhase

@Composable
fun StackTextLayout(modifier: Modifier = Modifier) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(Dimen.spacing),
        modifier = modifier.padding(horizontal = Dimen.spacing)
    ) {
        Text(
            stringResource(id = R.string.solo_draw_stack),
            textAlign = TextAlign.Center,
            modifier = Modifier.weight(1f)
        )
        Spacer(Modifier.weight(1f))
        Text(
            stringResource(id = R.string.solo_discard_stack),
            textAlign = TextAlign.Center,
            modifier = Modifier.weight(1f)
        )
    }
}

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun DrawCardsLayout(
    drawStack: Card?, // Mostly actions
    discardStack: Card?, // Mostly numbers
    activeCards: List<Card>, // Mostly numbers
    aiCards: @Composable (modifier: Modifier) -> Unit,
    gameType: GameType,
    modifier: Modifier = Modifier
) {
    val sharedTransitionScope = LocalSharedTransitionScope.current
        ?: throw IllegalStateException("No SharedElementScope found")
    val animatedVisibilityScope = LocalAnimatedVisibilityScope.current
        ?: throw IllegalStateException("No AnimatedVisibility found")

    with(sharedTransitionScope) {
        Column(modifier) {
            StackTextLayout()
            Row(
                horizontalArrangement = Arrangement.spacedBy(Dimen.spacing),
                modifier = Modifier
                    .padding(Dimen.spacing)
                    .weight(1f)
            ) {
                if (drawStack == null) {
                    Spacer(Modifier.weight(1f))
                } else {
                    Box(Modifier.weight(1f)) {
                        CardFaceDisplay(
                            drawStack.number,
                            drawStack.action,
                            modifier = Modifier.fillMaxSize()
                        )
                        activeCards.forEachIndexed { index, card ->
                            CardFaceDisplay(
                                card.number,
                                card.action,
                                modifier = Modifier
                                    .fillMaxSize()
                                    .sharedElement(
                                        rememberSharedContentState(key = "card_$index"),
                                        animatedVisibilityScope = animatedVisibilityScope
                                    )
                            )
                        }
                    }
                }
                Spacer(Modifier.weight(1f))
                if (discardStack == null) {
                    Spacer(Modifier.weight(1f))
                } else {
                    CardFaceDisplay(
                        discardStack.number,
                        discardStack.action,
                        modifier = Modifier.weight(1f)
                    )
                }
            }
            Text(
                stringResource(id = R.string.solo_action_instruction),
                minLines = 2,
                modifier = Modifier.padding(horizontal = Dimen.spacing)
            )
            Row(
                horizontalArrangement = Arrangement.spacedBy(Dimen.spacing),
                modifier = Modifier
                    .padding(Dimen.spacing)
                    .weight(1f)
            ) {
            }
            aiCards(
                Modifier
                    .weight(1f)
            )
        }
    }
}

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun SelectCardsLayout(
    drawStack: Card?, // Mostly actions
    discardStack: Card?, // Mostly numbers
    activeCards: List<Card>, // Mostly numbers
    aiCards: @Composable (modifier: Modifier) -> Unit,
    gameType: GameType,
    modifier: Modifier = Modifier
) {
    val sharedTransitionScope = LocalSharedTransitionScope.current
        ?: throw IllegalStateException("No SharedElementScope found")
    val animatedVisibilityScope = LocalAnimatedVisibilityScope.current
        ?: throw IllegalStateException("No AnimatedVisibility found")
    with(sharedTransitionScope) {
        Column(modifier) {
            StackTextLayout()
            Row(
                horizontalArrangement = Arrangement.spacedBy(Dimen.spacing),
                modifier = Modifier
                    .padding(Dimen.spacing)
                    .weight(1f)
            ) {
                if (drawStack == null) {
                    Spacer(Modifier.weight(1f))
                } else {
                    CardFaceDisplay(
                        drawStack.number,
                        drawStack.action,
                        modifier = Modifier.weight(1f)
                    )
                }
                Spacer(Modifier.weight(1f))
                if (discardStack == null) {
                    Spacer(Modifier.weight(1f))
                } else {
                    CardFaceDisplay(
                        discardStack.number,
                        discardStack.action,
                        modifier = Modifier.weight(1f)
                    )
                }
            }
            val playerSelectedCards = remember { mutableStateListOf<Int>() }
            var aiSelectedCard by remember { mutableStateOf<Int?>(null) }
            if (playerSelectedCards.size < 2) {
                Text(
                    stringResource(id = R.string.solo_action_instruction),
                    minLines = 2,
                    modifier = Modifier.padding(horizontal = Dimen.spacing)
                )
            } else {
                when (gameType) {
                    WelcomeToClassic -> {
                        Text(
                            stringResource(id = R.string.solo_action_instruction_aaa),
                            minLines = 2,
                            modifier = Modifier.padding(horizontal = Dimen.spacing)
                        )
                    }
                    WelcomeToTheMoon -> {
                        Text(
                            stringResource(id = R.string.solo_action_instruction_astra),
                            minLines = 2,
                            modifier = Modifier.padding(horizontal = Dimen.spacing)
                        )
                    }
                }
            }
            Row(
                horizontalArrangement = Arrangement.spacedBy(Dimen.spacing),
                modifier = Modifier
                    .padding(Dimen.spacing)
                    .weight(1f)
            ) {
                activeCards.forEachIndexed { index, card ->
                    val animatedAlpha: Float by animateFloatAsState(
                        if (playerSelectedCards.contains(index) || aiSelectedCard == index) {
                            0.5f
                        } else {
                            0f
                        },
                        label = "selection_alpha$index"
                    )
                    Box(Modifier.weight(1f)) {
                        CardFaceDisplay(
                            card.number,
                            card.action,
                            modifier = Modifier
                                .fillMaxSize()
                                .clickable {
                                    if (playerSelectedCards.size == 2) {
                                        aiSelectedCard = index
                                    } else {
                                        playerSelectedCards.add(index)
                                    }
                                }
                                .sharedElement(
                                    rememberSharedContentState(key = "card_$index"),
                                    animatedVisibilityScope = animatedVisibilityScope
                                )
                        )
                        // TODO fix corner radius
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .alpha(animatedAlpha)
                                .clip(RoundedCornerShape(Dimen.Card.radius))
                                .conditional(
                                    playerSelectedCards.contains(index),
                                    {
                                        background(Color.Green)
                                            .sharedElement(
                                                rememberSharedContentState(key = "discard_card_$index"),
                                                animatedVisibilityScope = animatedVisibilityScope
                                            )
                                    },
                                )
                                .conditional(
                                    aiSelectedCard == index,
                                    {
                                        background(Color.Red)
                                            .sharedElement(
                                                rememberSharedContentState(key = "ai_card_$index"),
                                                animatedVisibilityScope = animatedVisibilityScope
                                            )
                                    },
                                )
                        )
                    }
                }
            }
            aiCards(
                Modifier
                    .weight(1f)
            )
        }
    }
}

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun PlayerSelectionLayout(
    drawStack: Card?, // Mostly actions
    discardStack: Card?, // Mostly numbers
    activeCards: List<Card>, // Mostly numbers
    aiCards: @Composable (modifier: Modifier) -> Unit,
    gameType: GameType,
    modifier: Modifier = Modifier
) {
    val sharedTransitionScope = LocalSharedTransitionScope.current
        ?: throw IllegalStateException("No SharedElementScope found")
    val animatedVisibilityScope = LocalAnimatedVisibilityScope.current
        ?: throw IllegalStateException("No AnimatedVisibility found")
    with(sharedTransitionScope) {
        Column(modifier) {
            StackTextLayout()
            Row(
                horizontalArrangement = Arrangement.spacedBy(Dimen.spacing),
                modifier = Modifier
                    .padding(Dimen.spacing)
                    .weight(1f)
            ) {
                if (drawStack == null) {
                    Spacer(Modifier.weight(1f))
                } else {
                    CardFaceDisplay(
                        drawStack.number,
                        drawStack.action,
                        modifier = Modifier.weight(1f)
                    )
                }
                Spacer(Modifier.weight(1f))
                if (discardStack == null) {
                    Spacer(Modifier.weight(1f))
                } else {
                    Box(Modifier.weight(1f)) {
                        CardFaceDisplay(
                            discardStack.number,
                            discardStack.action,
                            modifier = Modifier.fillMaxSize()
                        )
                        activeCards.take(2).forEachIndexed { index, card ->
                            CardFaceDisplay(
                                card.number,
                                card.action,
                                modifier = Modifier
                                    .fillMaxSize()
                                    .sharedElement(
                                        rememberSharedContentState(key = "discard_card_$index"),
                                        animatedVisibilityScope = animatedVisibilityScope
                                    )
                            )
                        }
                    }
                }
            }

            Text(
                "",
                minLines = 2,
                modifier = Modifier.padding(horizontal = Dimen.spacing)
            )

            Row(
                horizontalArrangement = Arrangement.spacedBy(Dimen.spacing),
                modifier = Modifier
                    .padding(Dimen.spacing)
                    .weight(1f)
            ) {
            }
            Box(Modifier.weight(1f)) {
                aiCards(
                    Modifier
                        .fillMaxSize()
                )
                Row(
                    horizontalArrangement = Arrangement.spacedBy(Dimen.spacing),
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(Dimen.spacing)
                ) {
                    Spacer(Modifier.weight(1f))
                    CardFaceDisplay(
                        activeCards.last().number,
                        activeCards.last().action,
                        modifier = Modifier
                            .weight(1f)
                            .sharedElement(
                                rememberSharedContentState(key = "ai_card_2"),
                                animatedVisibilityScope = animatedVisibilityScope
                            )
                    )
                    Spacer(Modifier.weight(1f))
                }
            }
        }
    }
}

class PhasePreviewParameterProvider : PreviewParameterProvider<SoloGamePhase> {
    override val values: Sequence<SoloGamePhase> = sequenceOf(
        SoloGamePhase.DrawCards,
        SoloGamePhase.SelectCards,
        SoloGamePhase.PlayerSelection
    )
}

@OptIn(ExperimentalSharedTransitionApi::class)
@Preview(showBackground = true)
@Composable
fun SoloPhaseLayoutPreview(
    @PreviewParameter(PhasePreviewParameterProvider::class) phase: SoloGamePhase
) {
    val drawStack = Card(action = Astronaut, number = Number12)
    val discardStack = Card(action = Lightning, number = Number10)
    val activeCards = listOf(
        Card(action = X, number = Number1),
        Card(action = Plant, number = Number2),
        Card(action = Water, number = Number3)
    )
    val gameType = WelcomeToTheMoon
    val aiCards: @Composable (Modifier) -> Unit = { modifier ->
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
            modifier = modifier
                .height(400.dp)
                .padding(Dimen.spacing)
        )
    }
    WelcomeToFlipTheme {
        SharedTransitionLayout {
            CompositionLocalProvider(
                LocalSharedTransitionScope provides this
            ) {
                AnimatedContent(
                    phase,
                    label = "preview_transion"
                ) { _ ->
                    CompositionLocalProvider(LocalAnimatedVisibilityScope provides this) {
                        when (phase) {
                            is SoloGamePhase.AiSelection -> TODO()
                            SoloGamePhase.DrawCards -> {
                                DrawCardsLayout(
                                    drawStack = drawStack,
                                    discardStack = discardStack,
                                    activeCards = activeCards,
                                    gameType = gameType,
                                    aiCards = aiCards,
                                    modifier = Modifier.fillMaxSize()
                                )
                            }
                            is SoloGamePhase.EffectCardDrawn -> TODO()
                            SoloGamePhase.EndGame -> TODO()
                            SoloGamePhase.PlayerSelection -> {
                                PlayerSelectionLayout(
                                    drawStack = drawStack,
                                    discardStack = discardStack,
                                    activeCards = activeCards,
                                    gameType = gameType,
                                    aiCards = aiCards,
                                    modifier = Modifier.fillMaxSize()
                                )
                            }
                            SoloGamePhase.Reshuffle -> TODO()
                            SoloGamePhase.SelectCards -> {
                                SelectCardsLayout(
                                    drawStack = drawStack,
                                    discardStack = discardStack,
                                    activeCards = activeCards,
                                    gameType = gameType,
                                    aiCards = aiCards,
                                    modifier = Modifier.fillMaxSize()
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
