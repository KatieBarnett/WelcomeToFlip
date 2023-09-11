package dev.katiebarnett.welcometoflip.components

import androidx.compose.animation.core.CubicBezierEasing
import androidx.compose.animation.core.animate
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.layout.layoutId
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import dev.katiebarnett.welcometoflip.core.models.AstraA
import dev.katiebarnett.welcometoflip.core.models.AstraB
import dev.katiebarnett.welcometoflip.core.models.AstraC
import dev.katiebarnett.welcometoflip.core.models.Card
import dev.katiebarnett.welcometoflip.theme.Dimen
import dev.katiebarnett.welcometoflip.theme.WelcomeToFlipTheme
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch

internal const val PILE_ROTATION_AMOUNT = 5f // degrees

@Composable
fun PileInsertionLayout(
    pile: List<Card>,
    transitionTrigger: Boolean = false,
    onAnimationComplete: () -> Unit,
    modifier: Modifier = Modifier
) {
    val cardSpacing = with(LocalDensity.current) {
        Dimen.Card.spacing.toPx()
    }
    
    val offsetPercents = pile.map { remember { mutableStateOf(0f) }}
    val rotation = pile.mapIndexed { index, _ -> 
        remember { mutableStateOf(index * PILE_ROTATION_AMOUNT) }
    }

    val animationSpec = tween<Float>(1000, easing = CubicBezierEasing(0.4f, 0.0f, 0.8f, 0.8f))

    LaunchedEffect(key1 = transitionTrigger) {
        // Translate card to left position
        pile.forEachIndexed { index, _ ->
            coroutineScope {
                launch {
                    animate(initialValue = 0f, targetValue = 1f, animationSpec = animationSpec) { value: Float, _: Float ->
                        offsetPercents[index].value = value
                    }
                }
                launch {
                    animate(initialValue = index * PILE_ROTATION_AMOUNT, targetValue = 0f, animationSpec = animationSpec) { value: Float, _: Float ->
                        rotation[index].value = value
                    }
                }
            }
        }
        onAnimationComplete.invoke()
    }

    Layout(
        modifier = modifier
            .fillMaxSize(),
        content = {
            pile.forEachIndexed { index, card ->
                CardFaceDisplay(
                    cardFace = card.number,
                    modifier = Modifier
                        .zIndex((pile.size - index).toFloat())
                        .rotate(rotation[index].value)
                        .layoutId("PileCard")
                )
            }
        }) { measurables, constraints ->

        val pileCardPlaceables = measurables.filter { it.layoutId == "PileCard" }

        layout(constraints.maxWidth, constraints.maxHeight) {
            val cardWidth = (constraints.maxWidth / 2 - cardSpacing / 2).toInt()
            val pileConstraints = constraints.copy(
                minWidth = minOf(constraints.minWidth, cardWidth),
                maxWidth = cardWidth
            )

            val cardPositionEndX = 0
            val cardPositionStartX = cardPositionEndX + cardSpacing + cardWidth

            pileCardPlaceables.forEachIndexed { index, placeable ->
                val cardPositionX = (cardPositionStartX - (cardPositionStartX * offsetPercents[index].value)).toInt()
                placeable.measure(pileConstraints).place(cardPositionX, 0)
            }
        }
    }
}

@Preview(group = "Pile Components", showBackground = true)
@Composable
fun PileLayoutPreview() {
    
    val pile = listOf(
        Card(AstraA, AstraA),
        Card(AstraB, AstraB),
        Card(AstraC, AstraC)
    )

    WelcomeToFlipTheme {
        PileInsertionLayout(pile, false, {}, Modifier.height(400.dp).padding(Dimen.spacingDouble))
    }
}