package dev.veryniche.welcometoflip.components

import androidx.compose.animation.core.CubicBezierEasing
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
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
import androidx.compose.ui.layout.layoutId
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import dev.veryniche.welcometoflip.R
import dev.veryniche.welcometoflip.core.models.Action
import dev.veryniche.welcometoflip.core.models.SoloA
import dev.veryniche.welcometoflip.core.models.SoloB
import dev.veryniche.welcometoflip.core.models.SoloC
import dev.veryniche.welcometoflip.core.models.Astronaut
import dev.veryniche.welcometoflip.core.models.Card
import dev.veryniche.welcometoflip.core.models.Letter
import dev.veryniche.welcometoflip.core.models.Lightning
import dev.veryniche.welcometoflip.core.models.Number1
import dev.veryniche.welcometoflip.core.models.Number10
import dev.veryniche.welcometoflip.core.models.Number12
import dev.veryniche.welcometoflip.core.models.Number2
import dev.veryniche.welcometoflip.core.models.Number3
import dev.veryniche.welcometoflip.core.models.Plant
import dev.veryniche.welcometoflip.core.models.Robot
import dev.veryniche.welcometoflip.core.models.Water
import dev.veryniche.welcometoflip.core.models.X
import dev.veryniche.welcometoflip.theme.Dimen
import dev.veryniche.welcometoflip.theme.WelcomeToFlipTheme


private data class SoloSlotCoords(
    val drawStack: Coordinate,
    val discardStack: Coordinate,
    val activeCards: List<Coordinate>,
    val astraSection: Coordinate,
    val astraCard: Coordinate,
    val drawText: Coordinate,
    val discardText: Coordinate,
    val actionText: Coordinate,
    val astraText: Coordinate,
    val drawDiscardTextWidth: Dp,
    val cardHeight: Dp,
    val cardWidth: Dp,
    val astraHeight: Dp
) {
    data class Coordinate(
        val x: Dp,
        val y: Dp
    )
}

private fun getLayoutCoords(width: Dp, height: Dp, cardSpacing: Dp, textLineHeight: Dp, activeCardCount: Int): SoloSlotCoords {
    
    
    val columns = activeCardCount
    val rows = 3

    // Get text dimensions
    val drawDiscardTextWidth = width / 2
    val totalTextHeight = textLineHeight * 3

    // Card constraints
    val cardWidth = ((width - cardSpacing * (columns - 1)) / columns)
    val cardHeight = ((height - cardSpacing * (rows - 1) - totalTextHeight) / rows)
    
    val drawText = SoloSlotCoords.Coordinate(
        x = 0.dp, y = 0.dp
    )

    val discardText = SoloSlotCoords.Coordinate(
        x = drawDiscardTextWidth, y = 0.dp
    )
    
    val drawStack = SoloSlotCoords.Coordinate(
        x = 0.dp, y = textLineHeight
    )

    val discardStack = SoloSlotCoords.Coordinate(
        x = (cardSpacing + cardWidth) * (columns - 1), y = textLineHeight
    )
    
    val actionText = SoloSlotCoords.Coordinate(
        x = 0.dp, y = drawStack.y + cardHeight + cardSpacing
    )
    
    val activeCards = mutableListOf<SoloSlotCoords.Coordinate>()
    for (i in 0..activeCardCount) {
        activeCards.add(
            SoloSlotCoords.Coordinate(
                x = (cardSpacing + cardWidth) * i, y = actionText.y + textLineHeight
            )
        )
    }
    
    val astraText = SoloSlotCoords.Coordinate(
        x = 0.dp, y = activeCards.first().y + cardHeight + cardSpacing
    )
    
    val astraSection = SoloSlotCoords.Coordinate(
        x = 0.dp, y = astraText.y + textLineHeight
    )

    val astraCard = SoloSlotCoords.Coordinate(
        x = (width/2 - cardWidth/2), y = astraText.y + textLineHeight
    )

    return SoloSlotCoords(
        drawStack = drawStack,
        discardStack = discardStack,
        activeCards = activeCards,
        astraSection = astraSection,
        astraCard = astraCard,
        drawText = drawText,
        discardText = discardText,
        actionText = actionText,
        astraText = astraText,
        cardHeight = cardHeight,
        cardWidth = cardWidth,
        astraHeight = cardHeight,
        drawDiscardTextWidth = drawDiscardTextWidth
    )
}

private fun Modifier.offset(coords: SoloSlotCoords.Coordinate) = this.offset(coords.x, coords.y)

@Composable
fun SoloSlotLayout(
    drawStack: Card?, // Mostly actions
    discardStack: Card?, // Mostly numbers
    activeCards: List<Card>, // Mostly numbers
    astraCards: @Composable (modifier: Modifier) -> Unit,
    activeCardChoice: (Int) -> Unit,
    activeCardsDiscarded: List<Int>,
    activeCardToAstra: Int? = null,
    activeCardDiscardAnimationComplete: () -> Unit,
    astraCardAnimationComplete: () -> Unit,
    modifier: Modifier = Modifier
) {
    val instructionTextModifier = Modifier.height(Dimen.Solo.InstructionText.lineHeight)

    val animationSpec = tween<Dp>(1000, easing = CubicBezierEasing(0.4f, 0.0f, 0.8f, 0.8f))
    val animationSpecFlip = tween<Float>(1000, easing = CubicBezierEasing(0.4f, 0.0f, 0.8f, 0.8f))

    var offset by remember(activeCardToAstra) { mutableStateOf(0f) }
    var flipRotation by remember(activeCardToAstra) { mutableStateOf(0f) }
    
    
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
        
        Text(stringResource(id = R.string.solo_draw_stack), 
            modifier = instructionTextModifier
                .offset(initialLayoutCoords.drawText)
                .width(initialLayoutCoords.drawDiscardTextWidth))
        Text(stringResource(id = R.string.solo_discard_stack),
            textAlign = TextAlign.End,
            modifier = instructionTextModifier
                .offset(initialLayoutCoords.discardText)
                .width(initialLayoutCoords.drawDiscardTextWidth))
        Text(stringResource(id = R.string.solo_action_instruction), 
            modifier = instructionTextModifier.offset(initialLayoutCoords.actionText))
        Text(stringResource(id = R.string.solo_astra), 
            modifier = instructionTextModifier.offset(initialLayoutCoords.astraText))

        astraCards(modifier = Modifier
            .offset(initialLayoutCoords.astraSection)
            .height(initialLayoutCoords.astraHeight))
        
        if (drawStack?.action != null) {
            CardFaceDisplay(drawStack.action, null, modifier = cardModifier.offset(initialLayoutCoords.drawStack))
        } else {
            Spacer(modifier = cardModifier
                .layoutId("DrawStack")
                .offset(initialLayoutCoords.drawStack))
        }
        if (discardStack?.action != null) {
            CardFaceDisplay(discardStack.number, discardStack.action, modifier = cardModifier.offset(initialLayoutCoords.discardStack))
        } else {
            Spacer(modifier = cardModifier
                .layoutId("DiscardStack")
                .offset(initialLayoutCoords.discardStack))
        }
        
        activeCards.forEachIndexed { index, card ->

            val animatedDpX: Dp by animateDpAsState(
                targetValue = if (activeCardsDiscarded.contains(index)) {
                    initialLayoutCoords.discardStack.x
                } else if (activeCardToAstra == index) {
                    initialLayoutCoords.astraCard.x
                } else {
                    initialLayoutCoords.activeCards[index].x
                }, animationSpec = animationSpec, finishedListener = {
                    activeCardDiscardAnimationComplete.invoke()
                }
            )

            val animatedDpY: Dp by animateDpAsState(
                targetValue = if (activeCardsDiscarded.contains(index)) {
                    initialLayoutCoords.discardStack.y
                } else if (activeCardToAstra == index) {
                    initialLayoutCoords.astraCard.y
                } else {
                    initialLayoutCoords.activeCards[index].y
                }, animationSpec = animationSpec
            )
            
            CardFaceDisplay(card.number, card.action,
                modifier = cardModifier
                    .offset(x = animatedDpX, y = animatedDpY)
                    .zIndex(maxOf(activeCardsDiscarded.indexOf(index) + 1f, 0f))
                    .clickable(enabled = true, onClick = { 
                        activeCardChoice.invoke(index)
                    })
            )
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
                .padding(Dimen.spacing))
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
                .padding(Dimen.spacing))
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
                X to 5),
            effectCards = listOf(SoloA, SoloB, SoloC),
            modifier = Modifier
                .height(200.dp)
                .padding(Dimen.spacing))
    }
}

@Preview(group = "Solo Game Components", showBackground = true)
@Composable
fun SoloSlotLayoutPreview() {
    WelcomeToFlipTheme {
        SoloSlotLayout(
            drawStack = Card(action = Astronaut, number = Number12),
            discardStack = Card(action = Lightning, number = Number10),
            activeCards = listOf(
                Card(action = X, number = Number1),
                Card(action = Plant, number = Number2),
                Card(action = Water, number = Number3)),
            astraCards = {
                SoloAstraLayout(
                    astraCards = mapOf(
                        Plant to 0,
                        Water to 1,
                        Lightning to 2,
                        Robot to 3,
                        Astronaut to 4,
                        X to 5),
                    effectCards = listOf(SoloA, SoloB, SoloC),
                    modifier = it
                        .height(400.dp)
                        .padding(Dimen.spacing))
            },
            modifier = Modifier
                .height(1000.dp)
                .width(720.dp)
                .padding(Dimen.spacingDouble),
            activeCardChoice = { },
            activeCardsDiscarded = listOf(),
            activeCardDiscardAnimationComplete = {},
            astraCardAnimationComplete = {} 
        )
    }
}