package dev.katiebarnett.welcometoflip

import android.util.Log
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.tween
import androidx.compose.animation.core.updateTransition
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp


enum class BoxState { Front, Back }

@Composable
fun AnimatingBox(
    rotated: Boolean,
    onRotateComplete: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
    targetFrontCard: Int,
    targetBackCard: Int,
    cardSize: IntSize
) {
    val boxState = if (rotated) BoxState.Back else BoxState.Front
    val transitionData = updateTransitionData(boxState)
    Box(
        modifier
            .width(cardSize.width.dp)
            .height(cardSize.height.dp)
            .graphicsLayer {
                rotationY = transitionData.rotation
                cameraDistance = 8 * density
            }
    ) {
        Column(
            Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Image(
                painter = painterResource(
                    if (boxState == BoxState.Front && transitionData.rotation < 90f) {
                        targetFrontCard
                    } else if (boxState == BoxState.Back && transitionData.rotation > 90f) {
                        targetBackCard
                    } else if (boxState == BoxState.Front) {
                        targetBackCard
                    } else {
                        targetFrontCard
                    }),
                contentDescription = "Flip card"
            )
        }
    }
}


private class TransitionData(
    rotation: State<Float>,
    animateFront: State<Float>,
    animateBack: State<Float>
) {
    val rotation by rotation
    val animateFront by animateFront
    val animateBack by animateBack
}

@Composable
private fun updateTransitionData(boxState: BoxState): TransitionData {
    val transition = updateTransition(boxState, label = "")
    val rotation = transition.animateFloat(
        transitionSpec = {
            tween(500)
        },
        label = ""
    ) { state ->
        when (state) {
            BoxState.Front -> 0f
            BoxState.Back -> 180f
        }
    }

    val animateFront = transition.animateFloat(
        transitionSpec = {
            tween(500)
        },
        label = ""
    ) { state ->
        when (state) {
            BoxState.Front -> 1f
            BoxState.Back -> 0f
        }
    }
    val animateBack = transition.animateFloat(
        transitionSpec = {
            tween(500)
        },
        label = ""
    ) { state ->
        when (state) {
            BoxState.Front -> 0f
            BoxState.Back -> 1f
        }
    }

    return remember(transition) {
        TransitionData(
            rotation = rotation,
            animateFront = animateFront,
            animateBack = animateBack
        )
    }
}