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
        StackLayout(
            flipCard = viewModel.currentCard,
            numberStack = { modifier ->
                CardFaceDisplay(viewModel.numberStackTop?.number, viewModel.numberStackTop?.action, modifier)
            }, actionStack = { modifier ->
                CardFaceDisplay(viewModel.actionStackTop?.action, null, modifier)
            },
            transitionTrigger = position,
            modifier = modifier
        )
    }
}

@Composable
fun StackLayout(
    flipCard: Card?,
    numberStack: @Composable (modifier: Modifier) -> Unit,
    actionStack: @Composable (modifier: Modifier) -> Unit,
    transitionTrigger: Int = 0,
    modifier: Modifier = Modifier
) {
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
            numberStack(modifier = Modifier.layoutId("NumberStack"))
            actionStack(modifier = Modifier.layoutId("ActionStack"))
            flipCard?.let {
                val modifier = Modifier
                    .layoutId("FlipCard")
                    .graphicsLayer {
                        rotationY = flipRotation
                        cameraDistance = 8 * density
                    }
                if (flipRotation < 90f) {
                    CardFaceDisplay(flipCard.number, flipCard.action, modifier)
                } else {
                    // Rotate the action card back again so it does not appear reversed
                    CardFaceDisplay(flipCard.action, null,
                        modifier = modifier.graphicsLayer {
                            rotationY = 180f
                        }
                    )
                }
            }
        }) { measurables, constraints ->
        
        val flipCardPlaceable =
            measurables.firstOrNull { it.layoutId == "FlipCard" }
        val numberStackPlaceable =
            measurables.firstOrNull { it.layoutId == "NumberStack" }
        val actionStackPlaceable =
            measurables.firstOrNull { it.layoutId == "ActionStack"} 

        layout(constraints.maxWidth, constraints.maxHeight) {
            val cardSpacing = Dimen.Card.spacing.toPx()
            val cardWidth = ((constraints.maxWidth - cardSpacing) / 2).toInt()
            val cardConstraints = constraints.copy(
                minWidth = minOf(constraints.minWidth, cardWidth),
                maxWidth = cardWidth
            )
            
            val numberStackX = 0
            val actionStackX = numberStackX + cardSpacing + cardWidth
            val flipCardX = actionStackX * offset

            numberStackPlaceable?.measure(cardConstraints)?.place(numberStackX, 0)
            actionStackPlaceable?.measure(cardConstraints)?.place(actionStackX.toInt(), 0)
            flipCardPlaceable?.measure(cardConstraints)?.place(flipCardX.toInt(), 0)
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

@Preview(group = "Stack Components", showBackground = true)
@Composable
fun StackPreview() {
    WelcomeToFlipTheme {
        StackLayout(
            flipCard = Card(Astronaut, Number12),
            numberStack = { modifier ->
                CardFaceDisplay(Number6, Water, modifier)
            }, actionStack = { modifier ->
                CardFaceDisplay(Lightning, null, modifier)
            },
            modifier = Modifier
                .height(400.dp)
                .padding(Dimen.spacingDouble)
        )
    }
}

@Preview(group = "Stack Components", showBackground = true)
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

@Preview(group = "Stack Components", showBackground = true)
@Composable
fun StackPreviewWithEmptyAction() {
    WelcomeToFlipTheme {
        StackLayout(
            flipCard = Card(Astronaut, Number12),
            numberStack = { modifier ->
                CardFaceDisplay(Number6, Water, modifier)
            }, actionStack = { modifier ->
                CardFaceDisplay(null, null, modifier)
            },
            modifier = Modifier
                .height(400.dp)
                .padding(Dimen.spacingDouble)
        )
    }
}

@Preview(group = "Stack Components", showBackground = true)
@Composable
fun StackPreviewWithEmptyNumber() {
    WelcomeToFlipTheme {
        StackLayout(
            flipCard = Card(Astronaut, Number12),
            numberStack = { modifier ->
                CardFaceDisplay(null, null, modifier)
            }, actionStack = { modifier ->
                CardFaceDisplay(Lightning, null, modifier)
            },
            modifier = Modifier
                .height(400.dp)
                .padding(Dimen.spacingDouble)
        )
    }
}