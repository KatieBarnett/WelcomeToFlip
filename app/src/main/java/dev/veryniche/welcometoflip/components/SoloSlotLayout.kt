package dev.veryniche.welcometoflip.components

import androidx.compose.animation.core.CubicBezierEasing
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.layoutId
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
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
import dev.veryniche.welcometoflip.core.models.welcomeToClassicDeck
import dev.veryniche.welcometoflip.theme.Dimen
import dev.veryniche.welcometoflip.theme.WelcomeToFlipTheme
import dev.veryniche.welcometoflip.viewmodels.ActiveCardSelectionResult
import timber.log.Timber

@Composable
fun SoloSlotLayout(
    drawStack: Card?, // Mostly actions
    discardStack: Card?, // Mostly numbers
    activeCards: List<Card>, // Mostly numbers
    aiCards: @Composable (modifier: Modifier) -> Unit,
    onDrawCards: () -> Unit,
    onSelectAiCard: (Card) -> Unit,
    gameType: GameType,
    modifier: Modifier = Modifier
) {
    val instructionTextModifier = Modifier.height(Dimen.Solo.InstructionText.lineHeight)

    val animationSpec = tween<Dp>(1000, easing = CubicBezierEasing(0.4f, 0.0f, 0.8f, 0.8f))
    val animationSpecFlip = tween<Float>(1000, easing = CubicBezierEasing(0.4f, 0.0f, 0.8f, 0.8f))

    var inProgressDiscardCards by rememberSaveable { mutableStateOf(listOf<Card>()) }
    var inProgressAstraCard by rememberSaveable { mutableStateOf<Card?>(null) }

    val canDraw by remember {
        derivedStateOf {
            (inProgressDiscardCards.size == 2 && inProgressAstraCard != null) || activeCards.isEmpty()
        }
    }

    val onSelectCard: (Card) -> ActiveCardSelectionResult = { card ->
        if (inProgressDiscardCards.size == 2) {
            inProgressAstraCard = card
            ActiveCardSelectionResult.ASTRA
        } else {
            inProgressDiscardCards = inProgressDiscardCards.plus(card)
            ActiveCardSelectionResult.DISCARD
        }
    }

//    var offset by remember(activeCardToAstra) { mutableStateOf(0f) }
//    var flipRotation by remember(activeCardToAstra) { mutableStateOf(0f) }

    BoxWithConstraints(modifier = modifier.fillMaxSize()) {
        val initialLayoutCoords = getLayoutCoords(
            width = this.maxWidth,
            height = this.maxHeight,
            cardSpacing = Dimen.Card.spacing,
            textLineHeight = Dimen.Solo.InstructionText.lineHeight,
            activeCardCount = activeCards.size
        )

        val cardModifier = Modifier
            .width(initialLayoutCoords.cardWidth)
            .height(initialLayoutCoords.cardHeight)

        // Wrap up animation
//        LaunchedEffect(key1 = activeCardToAstra) {
//            activeCardToAstra?.let { cardIndex ->
//                coroutineScope {
//                    launch {
//                        // Translate card to astra area
//                        ani
//                        animate(initialValue = initialLayoutCoords.activeCards[cardIndex].x, targetValue = initialLayoutCoords.astraCard.x, animationSpec = animationSpec
//                        ) { value: Float, _: Float ->
//                            offset = value
//                        }
//                    }
//                    launch {
//                        // Translate card to astra area
//                        animate(initialValue = 0f, targetValue = 1f, animationSpec = animationSpec
//                        ) { value: Float, _: Float ->
//                            offset = value
//                        }
//                    }
//                    launch {
//                        // Do the flip
//                        animate(
//                            initialValue = 0f, targetValue = 180f, animationSpec = animationSpecFlip
//                        ) { value: Float, _: Float ->
//                            flipRotation = value
//                        }
//                    }
//                    astraCardAnimationComplete.invoke()
//                }
//            }
//        }

        Text(
            stringResource(id = R.string.solo_draw_stack),
            modifier = instructionTextModifier
                .offset(initialLayoutCoords.drawText)
                .width(initialLayoutCoords.drawDiscardTextWidth)
        )
        Text(
            stringResource(id = R.string.solo_discard_stack),
            textAlign = TextAlign.End,
            modifier = instructionTextModifier
                .offset(initialLayoutCoords.discardText)
                .width(initialLayoutCoords.drawDiscardTextWidth)
        )
        Text(
            stringResource(id = R.string.solo_action_instruction),
            modifier = instructionTextModifier.offset(initialLayoutCoords.actionText)
        )
        when (gameType) {
            WelcomeToClassic -> {
                Text(
                    stringResource(id = R.string.solo_aaa),
                    modifier = instructionTextModifier.offset(initialLayoutCoords.aiText)
                )
            }
            WelcomeToTheMoon -> {
                Text(
                    stringResource(id = R.string.solo_astra),
                    modifier = instructionTextModifier.offset(initialLayoutCoords.aiText)
                )
            }
        }

        aiCards(
            Modifier
                .offset(initialLayoutCoords.aiSection)
                .height(initialLayoutCoords.aiHeight)
        )

        if (drawStack?.action != null) {
            CardFaceDisplay(
                cardFace = drawStack.action,
                peek = null,
                modifier = cardModifier
                    .offset(initialLayoutCoords.drawStack)
                    .clickable {
                        if (canDraw) {
                            onDrawCards.invoke()
                        }
                    }
            )
        } else {
            Box(
                modifier = cardModifier
                    .layoutId("DrawStack")
                    .offset(initialLayoutCoords.drawStack)
            )
        }
        if (discardStack?.action != null) {
            CardFaceDisplay(
                discardStack.number,
                discardStack.action,
                modifier = cardModifier.offset(initialLayoutCoords.discardStack)
            )
        } else {
            Box(
                modifier = cardModifier
                    .layoutId("DiscardStack")
                    .offset(initialLayoutCoords.discardStack)
            )
        }

        activeCards.forEachIndexed { index, card ->

            LaunchedEffect(card, inProgressDiscardCards) {
                Timber.d("Move $card to discard")
            }

            LaunchedEffect(card, inProgressAstraCard) {
                Timber.d("Move $card to astra")
            }

            val animatedDpX: Dp by animateDpAsState(
                targetValue = initialLayoutCoords.activeCards[index].x,

//                if (activeCardsDiscarded.contains(index)) {
//                    initialLayoutCoords.discardStack.x
//                } else if (activeCardToAstra == index) {
//                    initialLayoutCoords.astraCard.x
//                } else {
//                    initialLayoutCoords.activeCards[index].x
//                },
                animationSpec = animationSpec,
                finishedListener = {
//                    activeCardDiscardAnimationComplete.invoke()
                }
            )

            val animatedDpY: Dp by animateDpAsState(
                targetValue = initialLayoutCoords.activeCards[index].y,

//                if (activeCardsDiscarded.contains(index)) {
//                    initialLayoutCoords.discardStack.y
//                } else if (activeCardToAstra == index) {
//                    initialLayoutCoords.astraCard.y
//                } else {
//                    initialLayoutCoords.activeCards[index].y
//                },
                animationSpec = animationSpec
            )

            CardFaceDisplay(
                cardFace = card.number,
                peek = card.action,
                modifier = cardModifier
                    .offset(x = animatedDpX, y = animatedDpY)
//                    .zIndex(maxOf(activeCardsDiscarded.indexOf(index) + 1f, 0f))
                    .clickable(enabled = true, onClick = {
                        val animation = onSelectCard.invoke(card)
                    })
            )
        }
    }
}

private data class SoloSlotCoords(
    val drawStack: Coordinate,
    val discardStack: Coordinate,
    val activeCards: List<Coordinate>,
    val aiSection: Coordinate,
    val astraCard: Coordinate,
    val drawText: Coordinate,
    val discardText: Coordinate,
    val actionText: Coordinate,
    val aiText: Coordinate,
    val drawDiscardTextWidth: Dp,
    val cardHeight: Dp,
    val cardWidth: Dp,
    val aiHeight: Dp
) {
    data class Coordinate(
        val x: Dp,
        val y: Dp
    )
}

private fun getLayoutCoords(
    width: Dp,
    height: Dp,
    cardSpacing: Dp,
    textLineHeight: Dp,
    activeCardCount: Int,
): SoloSlotCoords {
    val columns = activeCardCount
    val rows = 3

    // Get text dimensions
    val drawDiscardTextWidth = width / 2
    val totalTextHeight = textLineHeight * 3

    // Card constraints
    val cardWidth = ((width - cardSpacing * (columns - 1)) / columns)
    val cardHeight = ((height - cardSpacing * (rows - 1) - totalTextHeight) / rows)

    val drawText = SoloSlotCoords.Coordinate(
        x = 0.dp,
        y = 0.dp
    )

    val discardText = SoloSlotCoords.Coordinate(
        x = drawDiscardTextWidth,
        y = 0.dp
    )

    val drawStack = SoloSlotCoords.Coordinate(
        x = 0.dp,
        y = textLineHeight
    )

    val discardStack = SoloSlotCoords.Coordinate(
        x = (cardSpacing + cardWidth) * (columns - 1),
        y = textLineHeight
    )

    val actionText = SoloSlotCoords.Coordinate(
        x = 0.dp,
        y = drawStack.y + cardHeight + cardSpacing
    )

    val activeCards = mutableListOf<SoloSlotCoords.Coordinate>()
    for (i in 0..activeCardCount) {
        activeCards.add(
            SoloSlotCoords.Coordinate(
                x = (cardSpacing + cardWidth) * i,
                y = actionText.y + textLineHeight
            )
        )
    }

    val astraText = SoloSlotCoords.Coordinate(
        x = 0.dp,
        y = activeCards.first().y + cardHeight + cardSpacing
    )

    val astraSection = SoloSlotCoords.Coordinate(
        x = 0.dp,
        y = astraText.y + textLineHeight
    )

    val astraCard = SoloSlotCoords.Coordinate(
        x = (width / 2 - cardWidth / 2),
        y = astraText.y + textLineHeight
    )

    return SoloSlotCoords(
        drawStack = drawStack,
        discardStack = discardStack,
        activeCards = activeCards,
        aiSection = astraSection,
        astraCard = astraCard,
        drawText = drawText,
        discardText = discardText,
        actionText = actionText,
        aiText = astraText,
        cardHeight = cardHeight,
        cardWidth = cardWidth,
        aiHeight = cardHeight,
        drawDiscardTextWidth = drawDiscardTextWidth
    )
}

private fun Modifier.offset(coords: SoloSlotCoords.Coordinate) = this.offset(coords.x, coords.y)

@Preview(group = "Solo Game Components", showBackground = true)
@Composable
fun SoloSlotLayoutWelcomeToClassicPreview() {
    WelcomeToFlipTheme {
        SoloSlotLayout(
            drawStack = Card(action = Astronaut, number = Number12),
            discardStack = Card(action = Lightning, number = Number10),
            activeCards = listOf(
                Card(action = X, number = Number1),
                Card(action = Plant, number = Number2),
                Card(action = Water, number = Number3)
            ),
            gameType = WelcomeToClassic,
            aiCards = {
                SoloAAALayout(
                    aaaCards = welcomeToClassicDeck.subList(0, 33),
                    effectCards = listOf(SoloA, SoloB, SoloC),
                    modifier = it
                        .height(400.dp)
                        .padding(Dimen.spacing)
                )
            },
            modifier = Modifier
                .height(1000.dp)
                .width(720.dp)
                .padding(Dimen.spacingDouble),
            onDrawCards = { },
            onSelectAiCard = {}
        )
    }
}

@Preview(group = "Solo Game Components", showBackground = true)
@Composable
fun SoloSlotLayoutWelcomeToTheMoonPreview() {
    WelcomeToFlipTheme {
        SoloSlotLayout(
            drawStack = Card(action = Astronaut, number = Number12),
            discardStack = Card(action = Lightning, number = Number10),
            activeCards = listOf(
                Card(action = X, number = Number1),
                Card(action = Plant, number = Number2),
                Card(action = Water, number = Number3)
            ),
            gameType = WelcomeToTheMoon,
            aiCards = {
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
                    modifier = it
                        .height(400.dp)
                        .padding(Dimen.spacing)
                )
            },
            modifier = Modifier
                .height(1000.dp)
                .width(720.dp)
                .padding(Dimen.spacingDouble),
            onDrawCards = { },
            onSelectAiCard = {}
        )
    }
}

// @Preview(group = "Solo Game Components", showBackground = true)
// @Composable
// fun SoloSlotLayoutStartPreview() {
//    WelcomeToFlipTheme {
//        SoloSlotLayout(
//            drawStack = Card(action = Astronaut, number = Number12),
//            discardStack = Card(SoloB, SoloB),
//            activeCards = listOf(),
//            astraCards = {
//                SoloAstraLayout(
//                    astraCards = mapOf(
//                        Plant to 0,
//                        Water to 0,
//                        Lightning to 0,
//                        Robot to 0,
//                        Astronaut to 0,
//                        X to 0
//                    ),
//                    effectCards = listOf(SoloA,),
//                    modifier = it
//                        .height(400.dp)
//                        .padding(Dimen.spacing)
//                )
//            },
//            modifier = Modifier
//                .height(1000.dp)
//                .width(720.dp)
//                .padding(Dimen.spacingDouble),
//            onDrawCards = { },
//            onSelectAstraCard = {}
//        )
//    }
// }
