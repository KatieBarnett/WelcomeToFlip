package dev.veryniche.welcometoflip.components

import androidx.compose.animation.core.CubicBezierEasing
import androidx.compose.animation.core.animate
import androidx.compose.animation.core.tween
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalHapticFeedback

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun Tile(
    sideFront: @Composable (Modifier) -> Unit,
    sideBack: @Composable (Modifier) -> Unit,
    modifier: Modifier = Modifier,
) {
    var showingFront by remember { mutableStateOf<Boolean?>(true) }
    var flipRotation by remember { mutableFloatStateOf(0f) }
    val animationSpecFlip = tween<Float>(1000, easing = CubicBezierEasing(0.4f, 0.0f, 0.8f, 0.8f))
    LaunchedEffect(showingFront) {
        if (showingFront == false) {
            // Do the flip
            animate(
                initialValue = 0f,
                targetValue = 180f,
                animationSpec = animationSpecFlip
            ) { value: Float, _: Float ->
                flipRotation = value
            }
        } else if (showingFront == true && flipRotation > 90f) {
            animate(
                initialValue = 180f,
                targetValue = 0f,
                animationSpec = animationSpecFlip
            ) { value: Float, _: Float ->
                flipRotation = value
            }
        }
    }

    Box(
        modifier
            .combinedClickable(
                onClick = {
                    showingFront = !(showingFront ?: true)
                }
            )
    ) {
        val animatedModifier = Modifier
            .fillMaxSize()
            .graphicsLayer {
                rotationY = flipRotation
                cameraDistance = 8 * density
            }
        if (flipRotation < 90f) {
            sideFront.invoke(animatedModifier)
        } else {
            // Rotate the action card back again so it does not appear reversed
            sideBack.invoke(
                animatedModifier.graphicsLayer {
                    rotationY = 180f
                }
            )
        }
    }
}
