package dev.veryniche.welcometoflip.components

import androidx.compose.animation.core.CubicBezierEasing
import androidx.compose.animation.core.animate
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.layout.layoutId
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import dev.veryniche.welcometoflip.viewmodels.StackViewModel
import dev.veryniche.welcometoflip.core.models.Astronaut
import dev.veryniche.welcometoflip.core.models.Card
import dev.veryniche.welcometoflip.core.models.CardFace
import dev.veryniche.welcometoflip.core.models.Lightning
import dev.veryniche.welcometoflip.core.models.Number12
import dev.veryniche.welcometoflip.core.models.Number6
import dev.veryniche.welcometoflip.core.models.Water
import dev.veryniche.welcometoflip.theme.Dimen
import dev.veryniche.welcometoflip.theme.WelcomeToFlipTheme

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
    var offset by remember(transitionTrigger) { mutableFloatStateOf(0f) }
    var flipRotation by remember(transitionTrigger) { mutableFloatStateOf(0f) }
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
            numberStack(Modifier.layoutId("ActionStack"))
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