package dev.veryniche.welcometoflip.components

import androidx.compose.animation.core.CubicBezierEasing
import androidx.compose.animation.core.animate
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.layout.layoutId
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.zIndex
import dev.veryniche.welcometoflip.core.models.Card
import dev.veryniche.welcometoflip.theme.Dimen

@Composable
fun SoloGameSetup(
    stacks: List<List<Card>>,
    soloPile: List<Card>,
    onAnimationComplete: () -> Unit,
    modifier: Modifier = Modifier
) {
    val offsetPercents = stacks.map { remember { mutableStateOf(1f) } }

    val stackSpacing = with(LocalDensity.current) {
        Dimen.spacing.toPx()
    }

    val animationSpec = tween<Float>(1000, easing = CubicBezierEasing(0.4f, 0.0f, 0.8f, 0.8f))

    var combineAnimationTrigger by remember { mutableStateOf(false) }

    LaunchedEffect(key1 = combineAnimationTrigger) {
        if (combineAnimationTrigger) {
            stacks.forEachIndexed { index, _ ->
                animate(
                    initialValue = 1f,
                    targetValue = 0f,
                    animationSpec = animationSpec
                ) { value: Float, _: Float ->
                    offsetPercents[index].value = value
                }
            }
            onAnimationComplete.invoke()
        }
    }

    Layout(
        modifier = modifier.fillMaxSize(),
        content = {
            stacks.forEachIndexed { index, stack ->
                Box(
                    modifier = Modifier
                        .zIndex((stacks.size - index).toFloat())
                        .layoutId("Stack")
                ) {
                    if (index == stacks.size - 1) {
                        PileInsertionLayout(soloPile, onAnimationComplete = {
                            combineAnimationTrigger = true
                        })
                    }
                    BasicStackLayout(stack.first().action)
                }
            }
        }
    ) { measurables, constraints ->
        val stackPlaceables = measurables.filter { it.layoutId == "Stack" }

        layout(constraints.maxWidth, constraints.maxHeight) {
            val stackHeight = (constraints.maxHeight / stacks.size - (stackSpacing / (stacks.size - 1))).toInt()
            val stackConstraints = constraints.copy(
                minHeight = minOf(constraints.minHeight, stackHeight),
                maxHeight = minOf(constraints.maxHeight, stackHeight)
            )

            val initialYs = stackPlaceables.mapIndexed { index, _ ->
                stackHeight * index + stackSpacing * index
            }

            stackPlaceables.forEachIndexed { index, placeable ->
                placeable.measure(stackConstraints).place(0, (initialYs[index] * offsetPercents[index].value).toInt())
            }
        }
    }
}

// @Preview(group = "Solo Game Screen", showBackground = true)
// @Composable
// fun SoloGameSetupPreview() {
//
//    val stacks = listOf(
//        listOf(
//            Card(Robot, Number1),
//            Card(Lightning, Number1)
//        ),
//        listOf(
//            Card(Lightning, Number2),
//            Card(Plant, Number2)
//        ),
//        listOf(
//            Card(X, Number3),
//            Card(Astronaut, Number3),
//            Card(Water, Number3)
//        )
//    )
//
//    val soloPile = listOf(
//        Card(SoloA, SoloA),
//        Card(SoloB, SoloB),
//        Card(SoloC, SoloC)
//    )
//
//    WelcomeToFlipTheme {
//        SoloGameSetup(stacks, soloPile, {}, Modifier)
//    }
// }