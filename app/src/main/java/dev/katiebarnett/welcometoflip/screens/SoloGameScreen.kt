package dev.katiebarnett.welcometoflip.screens

import androidx.compose.animation.core.CubicBezierEasing
import androidx.compose.animation.core.animate
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.layout.layoutId
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.zIndex
import dev.katiebarnett.welcometoflip.SoloGamePhase
import dev.katiebarnett.welcometoflip.SoloGameViewModel
import dev.katiebarnett.welcometoflip.components.*
import dev.katiebarnett.welcometoflip.core.models.*
import dev.katiebarnett.welcometoflip.theme.Dimen
import dev.katiebarnett.welcometoflip.theme.WelcomeToFlipTheme
import dev.katiebarnett.welcometoflip.util.getStackSize
import dev.katiebarnett.welcometoflip.util.observeLifecycle


@Composable
fun SoloGameBody(viewModel: SoloGameViewModel, 
                 gameType: GameType,
                 seed: Long? = null,
                 initialPosition: Int? = null,
                 onGameEnd: () -> Unit,
                 modifier: Modifier = Modifier
) {
    val position by viewModel.position.observeAsState(initialPosition ?: viewModel.initialPosition)
    val advancePositionEnabled by viewModel.advancePositionEnabled.observeAsState(true)
    val isEndGame by viewModel.isEndGame.observeAsState(false)
    val gameSeed by rememberSaveable { mutableStateOf(seed ?: System.currentTimeMillis()) }

    var showEndGameDialog by remember { mutableStateOf(false) }
    if (isEndGame && showEndGameDialog == false) {
        showEndGameDialog = true
    }

    val phase by viewModel.phase.observeAsState(SoloGamePhase.SETUP)

    viewModel.observeLifecycle(LocalLifecycleOwner.current.lifecycle)
    viewModel.initialiseGame(gameType, gameSeed, position)

    GameContainer(
        displayPosition = position + 1,
        displayEndPosition = viewModel.stacks.getStackSize() ?: 0,
        gameType = gameType,
        advancePosition = {
            viewModel.advancePosition()
        },
        advancePositionEnabled = advancePositionEnabled,
        content = { contentModifier ->
            when (phase) {
                SoloGamePhase.SETUP -> SoloGameSetup(
                    position = position,
                    stacks = viewModel.stacks,
                    soloPile = viewModel.soloPile,
                    onAnimationComplete = {
                        viewModel.setupSoloStack()
                        viewModel.advancePosition()
                    },
                    modifier = contentModifier
                )
                SoloGamePhase.PLAY -> SoloGame(
                    position = position,
                    stacks = viewModel.stacks,
                    modifier = contentModifier
                )
            }
        },
        modifier = modifier
    )

    if (showEndGameDialog) {
        EndGameDialog(
            reshuffleStacks = {
                viewModel.reshuffleStacks()
            },
            endGame = {
                viewModel.endGame(onGameEnd)
            },
            onDismissRequest = {
                showEndGameDialog = false
            }
        )
    }
}

@Composable
fun SoloGameSetup(position: Int,
                  stacks: List<List<Card>>,
                  soloPile: List<Card>,
                  onAnimationComplete: () -> Unit,
                  modifier: Modifier = Modifier) {

    val offsetPercents = stacks.map { remember { mutableStateOf(1f) }}
    
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

    Layout(modifier = modifier.fillMaxSize(),
        content = {
            stacks.forEachIndexed { index, stack ->
                Box(modifier = Modifier
                    .zIndex((stacks.size - index).toFloat())
                    .layoutId("Stack")) {
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

@Composable
fun SoloGame(position: Int,
                stacks: List<List<Card>>,
                modifier: Modifier = Modifier) {
    Column(
        verticalArrangement = Arrangement.spacedBy(Dimen.spacing),
        modifier = modifier) {
        stacks.forEach { stack ->
            Stack(stack, position, modifier.weight(1f))
        }
    }
}

@Preview(showBackground = true)
@Composable
fun SoloGameSetupPreview() {

    val stacks = listOf(
        listOf(
            Card(Robot, Number1),
            Card(Lightning, Number1)
        ),
        listOf(
            Card(Lightning, Number2),
            Card(Plant, Number2)
        ),
        listOf(
            Card(X, Number3),
            Card(Astronaut, Number3),
            Card(Water, Number3)
        )
    )
    
    val soloPile = listOf(
        Card(AstraA, AstraA),
        Card(AstraB, AstraB),
        Card(AstraC, AstraC)
    )

    WelcomeToFlipTheme {
        SoloGameSetup(0, stacks, soloPile, {}, Modifier)
    }
}

@Preview(showBackground = true)
@Composable
fun SoloGamePreview() {

    val stacks = listOf(
        listOf(
            Card(Robot, Number1),
            Card(Lightning, Number1)
        ),
        listOf(
            Card(Lightning, Number2),
            Card(Plant, Number2)
        ),
        listOf(
            Card(X, Number3),
            Card(Astronaut, Number3),
            Card(Water, Number3)
        )
    )

    WelcomeToFlipTheme {
        SoloGame(0, stacks, Modifier)
    }
}