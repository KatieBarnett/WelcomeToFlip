package dev.katiebarnett.welcometoflip.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.layout.layoutId
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import dev.katiebarnett.welcometoflip.R
import dev.katiebarnett.welcometoflip.StackViewModel
import dev.katiebarnett.welcometoflip.data.*
import dev.katiebarnett.welcometoflip.models.Card
import kotlin.math.roundToInt

@Composable
fun Stack(stack: List<Card>, position: Int, modifier: Modifier = Modifier) {
    val viewModel = hiltViewModel<StackViewModel>()
    viewModel.setStack(stack)
    viewModel.setPosition(position)
    
    Box(
        modifier = modifier,
        contentAlignment = Alignment.Center
    ) { 
        TopCards(
            viewModel.numberStackTop,
            viewModel.actionStackTop,
            viewModel.nextNumberCard
        )
    }
}

@Composable
private fun TopCards(numberCard: Card?, actionCard: Card?,
                     nextNumberCard: Card?,
                     modifier: Modifier = Modifier
) {

//    val scope = rememberCoroutineScope()
//
//    var transitionEnabled by remember { mutableStateOf(false) }
//
//    var flipCardOffset by remember { mutableStateOf(0f) }
//
//    var cardSize by remember { mutableStateOf(IntSize.Zero) }
//    var numberDeckPosition by remember { mutableStateOf(Offset.Zero) }
//    var actionDeckPosition by remember { mutableStateOf(Offset.Zero) }
//
//    var flipCardVisibility by remember { mutableStateOf(false) }
//
//    var frontCardAlpha by remember { mutableStateOf(1.0f) }
//    var frontCardRotationY by remember { mutableStateOf(0f) }
//
//    var backCardAlpha by remember { mutableStateOf(1.0f) }
//    var backCardRotationY by remember { mutableStateOf(-180f) }


    
    StackLayout(numberCard = {
        if (numberCard != null) {
            CardFace(numberCard.number, numberCard.action)
        } else {
            CardPlaceholder()
        }
    },
    actionCard = {
        if (actionCard != null) {
            CardFace(actionCard.action, null)
        } else {
            CardPlaceholder()
        }
    })
    

//    
//    Box {
//        Row(
//            modifier = Modifier
//                .padding(dimensionResource(id = R.dimen.spacing))
//                .fillMaxWidth(),
//            horizontalArrangement = Arrangement.spacedBy(dimensionResource(id = R.dimen.card_spacing))
//        ) {
//            
//            if (numberCard != null) {
//                CardFace(numberCard.number, numberCard.action, modifier.weight(1f))
//            } else {
//                CardPlaceholder(modifier.weight(1f))
//            }
//            
//            if (numberCard != null) {
//                CardFace(numberCard.number, numberCard.action, modifier.weight(1f))
//            } else {
//                CardPlaceholder(modifier.weight(1f))
//            }
//
//            if (actionCard != null) {
//                CardFace(actionCard.action, null, modifier.weight(1f))
//            } else {
//                CardPlaceholder(modifier.weight(1f))
//            }
//        }
//            numberCard?.let {
//                Image(
//                    painter = painterResource(numberCard),
//                    contentDescription = "Number Deck",
//                    modifier = modifier
//                        .clickable(
//                            enabled = true,
//                            onClick = {
//                                transitionEnabled = !transitionEnabled
//                            }
//                        )
//                        .onGloballyPositioned { coordinates ->
//                            cardSize = coordinates.size
//                            numberDeckPosition = coordinates.positionInParent()
//                        }
//                )
//            }
//            actionCard?.let {
//                Image(
//                    painter = painterResource(actionCard),
//                    contentDescription = "Action Deck",
//                    modifier = modifier
//                    .onGloballyPositioned { coordinates ->
//                        actionDeckPosition = coordinates.positionInParent()
//                    }
//                )
//            }
//        }
//        if (flipCardFront != null && flipCardBack != null) {
//            AnimatingBox(
//                rotated = transitionEnabled, cardSize = cardSize,
//                onRotateComplete = {
//                    Log.d("HERE", "Rotation complete: $it")
//                },
//                modifier = modifier.offset(flipCardOffset.dp)
//                    .width(cardSize.width.dp)
//                    .height(cardSize.height.dp),
//                targetFrontCard = flipCardFront,
//                targetBackCard = flipCardBack
//            )
//        }
//    }
}

@Composable
fun StackLayout(
    numberCard: @Composable BoxScope.() -> Unit,
    actionCard: @Composable BoxScope.() -> Unit,
    modifier: Modifier = Modifier
) {
    val cardSpacing = with(LocalDensity.current) {
        dimensionResource(id = R.dimen.card_spacing).toPx()
    }

    Layout(
        modifier = Modifier
            .padding(dimensionResource(id = R.dimen.spacing))
            .fillMaxSize()   ,
        content = {
            Box(modifier = Modifier
                .layoutId("NumberCard"), content = numberCard)
            Box(modifier = Modifier
                .layoutId("ActionCard"), content = actionCard)
        }) { measurables, constraints ->
        
        val numberCardPlaceable =
            measurables.first { it.layoutId == "NumberCard" }
        val actionCardPlaceable =
            measurables.first { it.layoutId == "ActionCard"} 

        layout(constraints.maxWidth, constraints.maxHeight) {
            val cardWidth = (constraints.maxWidth / 2 - cardSpacing / 2).toInt()
            val childConstraints = constraints.copy(
                minWidth = minOf(constraints.minWidth, cardWidth),
                maxWidth = cardWidth
            )
            val numberCardX = 0
            val actionCardX = numberCardX + cardSpacing + cardWidth
            numberCardPlaceable.measure(childConstraints).place(0, 0)
            actionCardPlaceable.measure(childConstraints).place(actionCardX.toInt(), 0)
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