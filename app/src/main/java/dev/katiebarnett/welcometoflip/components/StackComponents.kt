package dev.katiebarnett.welcometoflip.components

import androidx.compose.animation.core.CubicBezierEasing
import androidx.compose.animation.core.animate
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.layout.layoutId
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import dev.katiebarnett.welcometoflip.StackViewModel
import dev.katiebarnett.welcometoflip.core.models.*
import dev.katiebarnett.welcometoflip.theme.Dimen
import dev.katiebarnett.welcometoflip.theme.WelcomeToFlipTheme

@Composable
fun Stack(stack: List<Card>,
          position: Int,
          modifier: Modifier = Modifier) {
    val viewModel = hiltViewModel<StackViewModel>()
    viewModel.setStack(stack)
    viewModel.setPosition(position)
    Box(
        modifier = modifier,
        contentAlignment = Alignment.Center
    ) { 
        TopCards(
            numberCardStack = viewModel.numberStackTop,
            actionCardStack = viewModel.actionStackTop,
            currentCard = viewModel.currentCard,
            transitionTrigger = position
        )
    }
}

@Composable
private fun TopCards(currentCard: Card?,
                     numberCardStack: Card?,
                     actionCardStack: Card?,
                     transitionTrigger: Int = 0,
                     modifier: Modifier = Modifier
) {
    StackLayout(
        currentCard = currentCard, 
        numberCardStack = {
            if (numberCardStack != null) {
                CardFaceDisplay(numberCardStack.number, numberCardStack.action)
            } else {
                CardDisplayPlaceholder()
            }
        }, actionCardStack = {
            if (actionCardStack != null) {
                CardFaceDisplay(actionCardStack.action, null)
            } else {
                CardDisplayPlaceholder()
            }
        }, transitionTrigger, modifier)
}

@Composable
fun StackLayout(
    currentCard: Card?,
    numberCardStack: @Composable BoxScope.() -> Unit,
    actionCardStack: @Composable BoxScope.() -> Unit,
    transitionTrigger: Int = 0,
    modifier: Modifier = Modifier
) {
    val cardSpacing = with(LocalDensity.current) {
        Dimen.Card.spacing.toPx()
    }

    var offset by remember(transitionTrigger) { mutableStateOf(0f) }
    var flipRotation by remember(transitionTrigger) { mutableStateOf(0f) }
    val animationSpec = tween<Float>(1000, easing = CubicBezierEasing(0.4f, 0.0f, 0.8f, 0.8f))
    val animationSpecFlip = tween<Float>(1000, easing = CubicBezierEasing(0.4f, 0.0f, 0.8f, 0.8f))

    LaunchedEffect(key1 = transitionTrigger) {
        // Translate card to right stack
        animate(initialValue = 0f, targetValue = 1f, animationSpec = animationSpec) { value: Float, _: Float ->
            offset = value
        }
        // Do the flip
        animate(initialValue = 0f, targetValue = 180f, animationSpec = animationSpecFlip) { value: Float, _: Float ->
            flipRotation = value
        }
    }

    Layout(
        modifier = modifier
            .fillMaxSize(),
        content = {
            Box(modifier = Modifier
                .layoutId("NumberStack"), content = numberCardStack)
            Box(modifier = Modifier
                .layoutId("ActionStack"), content = actionCardStack)
            currentCard?.let {
                Box(modifier = Modifier
                    .layoutId("CurrentCardAnimated")
                    .graphicsLayer {
                        rotationY = flipRotation
                        cameraDistance = 8 * density
                    }
                , content = {
                    if (flipRotation < 90f) {
                        CardFaceDisplay(currentCard.number, currentCard.action)
                    } else {
                        // Rotate the action card back again so it does not appear reversed
                        CardFaceDisplay(currentCard.action, null, 
                            modifier = Modifier.graphicsLayer { 
                                rotationY = 180f 
                            }
                        )
                    }
                })
            }
        }) { measurables, constraints ->
        
        val currentCardAnimatedPlaceable =
            measurables.firstOrNull { it.layoutId == "CurrentCardAnimated" }
        val numberStackPlaceable =
            measurables.firstOrNull { it.layoutId == "NumberStack" }
        val actionStackPlaceable =
            measurables.firstOrNull { it.layoutId == "ActionStack"} 

        layout(constraints.maxWidth, constraints.maxHeight) {
            val cardWidth = (constraints.maxWidth / 2 - cardSpacing / 2).toInt()
            val stackConstraints = constraints.copy(
                minWidth = minOf(constraints.minWidth, cardWidth),
                maxWidth = cardWidth
            )
            
            val numberStackX = 0
            val actionStackX = numberStackX + cardSpacing + cardWidth
            val currentCardAnimatedX = actionStackX * offset

            numberStackPlaceable?.measure(stackConstraints)?.place(numberStackX, 0)
            actionStackPlaceable?.measure(stackConstraints)?.place(actionStackX.toInt(), 0)
            currentCardAnimatedPlaceable?.measure(stackConstraints)?.place(currentCardAnimatedX.toInt(), 0)
        }
    }
}

@Composable
fun BasicStackLayout(
    cardFace: CardFace,
    peek: CardFace? = null,
    modifier: Modifier = Modifier
) {
    Row(horizontalArrangement = Arrangement.spacedBy(Dimen.Card.spacing),
        modifier = modifier) {
        CardFaceDisplay(cardFace, peek, modifier = Modifier.weight(1f))
        Spacer(modifier = Modifier.weight(1f))
    }
}

@Preview(showBackground = true)
@Composable
fun StackPreview() {
    WelcomeToFlipTheme {
        TopCards(
            currentCard = Card(Astronaut, Number12),
            numberCardStack = Card(Water, Number6),
            actionCardStack = Card(Lightning, Number1), 
            modifier = Modifier
                .height(400.dp)
                .padding(Dimen.spacingDouble)
        )
    }
}

@Preview(showBackground = true)
@Composable
fun BasicStackLayoutPreview() {
    WelcomeToFlipTheme {
        BasicStackLayout(
            cardFace = Astronaut,
            modifier = Modifier
                .height(400.dp)
                .padding(Dimen.spacingDouble)
        )
    }
}

@Preview(showBackground = true)
@Composable
fun StackPreviewWithEmptyAction() {
    WelcomeToFlipTheme {
        TopCards(Card(Astronaut, Number12), null, null, modifier = Modifier
            .height(400.dp)
            .padding(Dimen.spacingDouble))
    }
}

@Preview(showBackground = true)
@Composable
fun StackPreviewWithEmptyNumber() {
    WelcomeToFlipTheme {
        TopCards(null, Card(Water, Number6), null, modifier = Modifier
            .height(400.dp)
            .padding(Dimen.spacingDouble))
    }
}