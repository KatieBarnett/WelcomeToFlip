package dev.katiebarnett.welcometoflip.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.tooling.preview.Preview
import dev.katiebarnett.welcometoflip.SoloGamePhase
import dev.katiebarnett.welcometoflip.SoloGameViewModel
import dev.katiebarnett.welcometoflip.components.CardFaceDisplay
import dev.katiebarnett.welcometoflip.components.EndGameDialog
import dev.katiebarnett.welcometoflip.components.GameContainer
import dev.katiebarnett.welcometoflip.components.Stack
import dev.katiebarnett.welcometoflip.core.models.*
import dev.katiebarnett.welcometoflip.theme.Dimen
import dev.katiebarnett.welcometoflip.theme.WelcomeToFlipTheme
import dev.katiebarnett.welcometoflip.util.getStackSize
import dev.katiebarnett.welcometoflip.util.observeLifecycle

internal const val PILE_ROTATION_AMOUNT = 5f // degrees

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
                SoloGamePhase.SETUP -> RegularGame(
                    position = position,
                    stacks = viewModel.stacks,
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
                  soloStack: List<Card>,
                  modifier: Modifier = Modifier) {

//    val dbAnimateAsState: Dp by animateDpAsState(
//        targetValue = switch(enabled),
//        animationSpec = animationSpec()
//    )
//    
    Box {
        Column(verticalArrangement = Arrangement.spacedBy(Dimen.Button.spacing),
            modifier = modifier) {
            stacks.forEachIndexed { index, stack ->
                Box(Modifier.weight(1f)) {

                    var cardHeight by rememberSaveable { mutableStateOf("") }
                    Stack(
                        stack = stack,
                        position = position,
                        modifier = Modifier
                    )
                    if (index == stacks.size - 1) {
                        soloStack.reversed().forEachIndexed { index, card ->
                            val stackIndex = soloStack.size - 1 - index
                            CardFaceDisplay(
                                cardFace = card.number,
                                modifier = Modifier
                                    .rotate(stackIndex * PILE_ROTATION_AMOUNT)
                            )
                        }
                    }
                }
            }
        }
    }
}

//@Composable
//fun SoloGameSetup(position: Int,
//                  stacks: List<List<Card>>,
//                  soloStack: List<Card>, 
//                  modifier: Modifier = Modifier) {
//
////    val dbAnimateAsState: Dp by animateDpAsState(
////        targetValue = switch(enabled),
////        animationSpec = animationSpec()
////    )
////    
//    Box {
//        Column(modifier = modifier) {
//            stacks.forEachIndexed { index, stack ->
//                Layout(
//                    content = {
//                        Stack(stack = stack, 
//                            position = position,
//                            modifier = modifier
//                                .weight(1f)
//                                .layoutId("Stack")
//                        )
//                        if (index == stacks.size - 1) {
//                            soloStack.reversed().forEachIndexed { index, card ->
//                                val stackIndex = soloStack.size - 1 - index
//                                CardFaceDisplay(
//                                    cardFace = card.number,
//                                    modifier = Modifier
//                                        .rotate(stackIndex * PILE_ROTATION_AMOUNT)
//                                        .layoutId("PileCard")
//                                )
//                            }
//                        }
//                    }) { measurables, constraints ->
//                    val stackPlaceable =
//                        measurables.firstOrNull { it.layoutId == "CurrentCardAnimated" }
//                    val pilePlaceables =
//                        measurables.filter { it.layoutId == "PileCard" }
//
//                    layout(constraints.maxWidth, constraints.maxHeight) {
//                        val cardWidth = (constraints.maxWidth / 2 - cardSpacing / 2).toInt()
//                        val pileConstraints = constraints.copy(
//                            minWidth = stackPlaceable
//                            maxWidth = cardWidth
//                        )
//
//                        val numberStackX = 0
//                        val actionStackX = numberStackX + cardSpacing + cardWidth
//                        val currentCardAnimatedX = actionStackX * offset
//
//                        numberStackPlaceable?.measure(stackConstraints)?.place(numberStackX, 0)
//                        actionStackPlaceable?.measure(stackConstraints)?.place(actionStackX.toInt(), 0)
//                        currentCardAnimatedPlaceable?.measure(stackConstraints)?.place(currentCardAnimatedX.toInt(), 0)
//                    }
//            }
//            }
//        }
////        Pile(pile = soloStack)
//    }
//}

@Composable
fun SoloGame(position: Int,
                stacks: List<List<Card>>,
                modifier: Modifier = Modifier) {
    Column(
        verticalArrangement = Arrangement.spacedBy(Dimen.Button.spacing),
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
    
    val soloStack = listOf(
        Card(AstraA, AstraA),
        Card(AstraB, AstraB),
        Card(AstraC, AstraC)
    )

    WelcomeToFlipTheme {
        SoloGameSetup(0, stacks, soloStack, Modifier)
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