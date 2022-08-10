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
import dev.katiebarnett.welcometoflip.SoloState
import dev.katiebarnett.welcometoflip.components.*
import dev.katiebarnett.welcometoflip.core.models.*
import dev.katiebarnett.welcometoflip.theme.Dimen
import dev.katiebarnett.welcometoflip.theme.WelcomeToFlipTheme
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
    val currentState by viewModel.currentState.observeAsState(SoloState())
    val activeCardToAstra by viewModel.activeCardToAstra.observeAsState(null)

    viewModel.observeLifecycle(LocalLifecycleOwner.current.lifecycle)
    viewModel.initialiseGame(gameType, gameSeed, position)

    SoloGameContainer(
        displayPosition = position + 1,
        displayEndPosition = currentState.totalPosition, 
        gameType = gameType,
        content = { contentModifier ->
            when (phase) {
                SoloGamePhase.SETUP -> SoloGameSetup(
                    stacks = viewModel.stacks,
                    soloPile = viewModel.soloEffectCards,
                    onAnimationComplete = {
                        viewModel.setupSoloDrawStack()
                        viewModel.setupAstraCards()
                        viewModel.advancePosition()
                    },
                    modifier = contentModifier
                )
                SoloGamePhase.PLAY -> SoloGame(
                    state = currentState,
                    activeCardChoice = {
                        viewModel.handleActiveCardClick(it)
                    },
                    activeCardsDiscarded = currentState.cardsDiscarded,
                    activeCardDiscardAnimationComplete = {
                        viewModel.checkRemainingActiveCards()
                    },
                    activeCardToAstra = currentState.cardDiscardedToAstra,
                    modifier = contentModifier,
                    astraCardAnimationComplete = {}
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
fun SoloGameSetup(stacks: List<List<Card>>,
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
fun SoloGame(state: SoloState,
             activeCardChoice: (Int) -> Unit,
             activeCardToAstra: Int?,
             activeCardsDiscarded: List<Int>,
             astraCardAnimationComplete: () -> Unit,
             activeCardDiscardAnimationComplete: () -> Unit,
             modifier: Modifier = Modifier) {

    SoloSlotLayout(
        drawStack = state.drawStackTopCard,
        discardStack = state.discardStackTopCard,
        activeCards = state.activeCards,
        astraCards = {
            SoloAstraLayout(
                astraCards = state.astraCards,
                effectCards = state.effectCards,
                modifier = it)
        },
        activeCardChoice = activeCardChoice,
        activeCardToAstra = activeCardToAstra,
        activeCardsDiscarded = activeCardsDiscarded,
        astraCardAnimationComplete = astraCardAnimationComplete,
        activeCardDiscardAnimationComplete = activeCardDiscardAnimationComplete,
        modifier = modifier
    )
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
        SoloGameSetup(stacks, soloPile, {}, Modifier)
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

//    WelcomeToFlipTheme {
//        SoloGame(SoloState(
//            drawStackTopCard = Card(action = Astronaut, number = Number12),
//            discardStackTopCard = Card(action = Lightning, number = Number10),
//            activeCards = listOf(
//                Card(action = X, number = Number1),
//                Card(action = Plant, number = Number2),
//                Card(action = Water, number = Number3)),
//            astraCards = mapOf(
//                Plant to 0,
//                Water to 1,
//                Lightning to 2,
//                Robot to 3,
//                Astronaut to 4,
//                X to 5),
//            effectCards = listOf(AstraA, AstraB, AstraC)
//        ),
//            astraCardAnimationComplete = {})
//    }
}