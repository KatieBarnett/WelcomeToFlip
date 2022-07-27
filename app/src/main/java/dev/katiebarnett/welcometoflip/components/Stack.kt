package dev.katiebarnett.welcometoflip.components

import android.util.Log
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
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import dev.katiebarnett.welcometoflip.R
import dev.katiebarnett.welcometoflip.StackViewModel
import dev.katiebarnett.welcometoflip.data.*
import dev.katiebarnett.welcometoflip.models.Card
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch

@Composable
fun Stack(stack: List<Card>, 
          position: Int,
          transitionEnabled: Boolean = false, 
          modifier: Modifier = Modifier) {
    val viewModel = hiltViewModel<StackViewModel>()
    viewModel.setStack(stack)
    viewModel.setPosition(position)

    Log.d("flipRotation", "New position: $position")
    Box(
        modifier = modifier,
        contentAlignment = Alignment.Center
    ) { 
        TopCards(
            numberCardStack = viewModel.numberStackTop,
            actionCardStack = viewModel.actionStackTop,
            currentCard = viewModel.currentCard,
            transitionEnabled = transitionEnabled
        )
    }
}

@Composable
private fun TopCards(currentCard: Card?,
                     numberCardStack: Card?, 
                     actionCardStack: Card?,
                     transitionEnabled: Boolean = false,
                     modifier: Modifier = Modifier
) {
    StackLayout(
        currentCard = currentCard, 
        numberCardStack = {
            if (numberCardStack != null) {
                CardFace(numberCardStack.number, numberCardStack.action)
            } else {
                CardPlaceholder()
            }
        }, actionCardStack = {
            if (actionCardStack != null) {
                CardFace(actionCardStack.action, null)
            } else {
                CardPlaceholder()
            }
        }, transitionEnabled, modifier)
}

@Composable
fun StackLayout(
    currentCard: Card?,
    numberCardStack: @Composable BoxScope.() -> Unit,
    actionCardStack: @Composable BoxScope.() -> Unit,
    transitionEnabled: Boolean = false,
    modifier: Modifier = Modifier
) {
    val cardSpacing = with(LocalDensity.current) {
        dimensionResource(id = R.dimen.card_spacing).toPx()
    }

    val scope = rememberCoroutineScope()
    var offset by remember { mutableStateOf(0f) }
    var flipRotation by remember { mutableStateOf(0f) }
    
    val animationSpec = tween<Float>(1000, easing = CubicBezierEasing(0.4f, 0.0f, 0.8f, 0.8f))
    val animationSpecFlip = tween<Float>(3000, easing = CubicBezierEasing(0.4f, 0.0f, 0.8f, 0.8f))

    LaunchedEffect(key1 = transitionEnabled) {
        scope.launch {
            // Core animation
            coroutineScope {
                launch {
                    offset = 0f
                    flipRotation = 0f
                    // Translate card to right stack
                    animate(initialValue = 0f, targetValue = 1f, animationSpec = animationSpec) { value: Float, _: Float ->
                        offset = value
                    }
                    // Do the flip
                    animate(initialValue = 0f, targetValue = 180f, animationSpec = animationSpecFlip) { value: Float, _: Float ->
                        flipRotation = value
                    }
                }
            }
        }
    }
    
    Layout(
        modifier = Modifier
            .padding(dimensionResource(id = R.dimen.spacing))
            .fillMaxSize()   ,
        content = {
            Box(modifier = Modifier
                .layoutId("NumberStack"), content = numberCardStack)
            Box(modifier = Modifier
                .layoutId("ActionStack"), content = actionCardStack)
            
            currentCard?.let {
                Box(modifier = Modifier
                    .layoutId("CurrentCard")
                    .graphicsLayer {
                        rotationY = flipRotation
                        cameraDistance = 8 * density
                    }
            , content = {
                    if (flipRotation < 90f) {
                        CardFace(currentCard.number, currentCard.action)
                    } else {
                        CardFace(currentCard.action, null)
                    }
                }
                )
            }
        }) { measurables, constraints ->
        
        val currentCardPlaceable =
            measurables.firstOrNull { it.layoutId == "CurrentCard" }
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

//            val currentCardConstraints = constraints.copy(
//                minWidth = minOf(constraints.minWidth, (cardWidth * flipWidth).toInt()),
//                maxWidth = (cardWidth * flipWidth).toInt()
//            )
            
            val numberStackX = 0
            val actionStackX = numberStackX + cardSpacing + cardWidth
            val currentCardX = actionStackX * offset

            numberStackPlaceable?.measure(stackConstraints)?.place(numberStackX, 0)
            actionStackPlaceable?.measure(stackConstraints)?.place(actionStackX.toInt(), 0)
            currentCardPlaceable?.measure(stackConstraints)?.place(currentCardX.toInt(), 0)
        }
    }
}

@Preview(showBackground = true)
@Composable
fun StackPreview() {
    TopCards(Card(Astronaut, Number12), Card(Water, Number6), Card(Lightning, Number1), modifier = Modifier.height(400.dp))
}

@Preview(showBackground = true)
@Composable
fun StackPreviewWithEmptyAction() {
    TopCards(Card(Astronaut, Number12), null, null, modifier = Modifier.height(400.dp))
}

@Preview(showBackground = true)
@Composable
fun StackPreviewWithEmptyNumber() {
    TopCards(null, Card(Water, Number6), null, modifier = Modifier.height(400.dp))
}