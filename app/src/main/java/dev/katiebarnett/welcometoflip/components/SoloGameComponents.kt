package dev.katiebarnett.welcometoflip.components

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.layout.layoutId
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import dev.katiebarnett.welcometoflip.R
import dev.katiebarnett.welcometoflip.core.models.*
import dev.katiebarnett.welcometoflip.theme.Dimen
import dev.katiebarnett.welcometoflip.theme.WelcomeToFlipTheme

@Composable
fun SoloSlotLayout(
    drawStack: Card, // Mostly actions
    discardStack: Card, // Mostly numbers
    activeCards: List<Card>, // Mostly numbers
    astraCards: @Composable (modifier: Modifier) -> Unit,
    modifier: Modifier = Modifier
) {
    val cardSpacing = with(LocalDensity.current) {
        Dimen.Card.spacing.toPx()
    }
    
    val instructionTextModifier = Modifier.padding(bottom = Dimen.Solo.InstructionText.paddingBottom)
    
    Layout(
        modifier = modifier
            .fillMaxSize(),
        content = {
            Text(stringResource(id = R.string.solo_draw_stack), 
                modifier = instructionTextModifier.layoutId("DrawText"))
            Text(stringResource(id = R.string.solo_discard_stack),
                textAlign = TextAlign.End,
                modifier = instructionTextModifier.layoutId("DiscardText"))
            Text(stringResource(id = R.string.solo_action_instruction), 
                modifier = instructionTextModifier.layoutId("ActionText"))
            Text(stringResource(id = R.string.solo_astra), 
                modifier = instructionTextModifier.layoutId("AstraText"))
            
            CardFaceDisplay(drawStack.action, null, modifier = Modifier.layoutId("DrawStack"))
            CardFaceDisplay(discardStack.number, discardStack.action, modifier = Modifier.layoutId("DiscardStack"))
            activeCards.forEachIndexed { index, card ->
                CardFaceDisplay(card.number, card.action, modifier = Modifier.layoutId("ActiveCard$index"))
            }
            
            astraCards(modifier = Modifier.layoutId("AstraCards"))
            
        }) { measurables, constraints ->

        val drawStackPlaceable =
            measurables.firstOrNull { it.layoutId == "DrawStack" }
        val discardStackPlaceable =
            measurables.firstOrNull { it.layoutId == "DiscardStack" }
        val actionStackPlaceables =
            measurables.filter { it.layoutId is String && (it.layoutId as String).startsWith("ActiveCard")}
        val astraCardsPlaceable =
            measurables.firstOrNull { it.layoutId == "AstraCards" }
        
        val drawTextPlaceable =
            measurables.firstOrNull { it.layoutId == "DrawText" }
        val discardTextPlaceable =
            measurables.firstOrNull { it.layoutId == "DiscardText" }
        val actionTextPlaceable =
            measurables.firstOrNull { it.layoutId == "ActionText" }
        val astraTextPlaceable =
            measurables.firstOrNull { it.layoutId == "AstraText" }

        layout(constraints.maxWidth, constraints.maxHeight) {
            val columns = activeCards.size
            val rows = 3
            
            // Get text heights
            val drawDiscardTextWidth = constraints.maxWidth / 2
            val drawDiscardTextHeight = maxOf(drawTextPlaceable?.maxIntrinsicHeight(drawDiscardTextWidth) ?: 0, discardTextPlaceable?.maxIntrinsicHeight(drawDiscardTextWidth) ?: 0)

            val actionTextHeight = (actionTextPlaceable?.maxIntrinsicHeight(constraints.maxWidth) ?: 0)
            val astraTextHeight = (actionTextPlaceable?.maxIntrinsicHeight(constraints.maxWidth) ?: 0)
            
            val totalTextHeight = drawDiscardTextHeight + actionTextHeight + astraTextHeight
            
            // Text constraints
            val drawDiscardTextConstraints = constraints.copy(
                minWidth = minOf(constraints.minWidth, drawDiscardTextWidth),
                maxWidth = drawDiscardTextWidth
            )

            // Card constraints
            val cardWidth = ((constraints.maxWidth - cardSpacing * (columns - 1)) / columns).toInt()
            val cardHeight = ((constraints.maxHeight - cardSpacing * (rows - 1) - totalTextHeight) / rows).toInt()
            val cardConstraints = constraints.copy(
                minWidth = minOf(constraints.minWidth, cardWidth),
                maxWidth = cardWidth,
                minHeight = minOf(constraints.minHeight, cardHeight),
                maxHeight = cardHeight
            )
            
            // Astra section constraints
            val astraCardsConstraints = constraints.copy(
                minHeight = minOf(constraints.minHeight, cardHeight),
                maxHeight = cardHeight
            )

            val discardStackX = (cardSpacing + cardWidth) * (columns - 1)
            
            var updatedY = 0

            drawTextPlaceable?.measure(drawDiscardTextConstraints)?.place(0, updatedY)
            discardTextPlaceable?.measure(drawDiscardTextConstraints)?.place(drawDiscardTextWidth, updatedY)

            updatedY = drawDiscardTextHeight
            
            drawStackPlaceable?.measure(cardConstraints)?.place(0, updatedY)
            discardStackPlaceable?.measure(cardConstraints)?.place(discardStackX.toInt(), updatedY)

            updatedY += (cardSpacing + cardHeight).toInt()

            actionTextPlaceable?.measure(constraints)?.place(0, updatedY)

            updatedY += actionTextHeight
            
            actionStackPlaceables.forEachIndexed { index, actionStack ->
                actionStack.measure(cardConstraints).place(((cardSpacing + cardWidth) * index).toInt(), updatedY)
            }

            updatedY += (cardSpacing + cardHeight).toInt()

            astraTextPlaceable?.measure(constraints)?.place(0, updatedY)

            updatedY += astraTextHeight
            
            astraCardsPlaceable?.measure(astraCardsConstraints)?.place(0, updatedY)
            
            // End position of the astra animation
            // TODO
        }
    }
}

@Preview(showBackground = true)
@Composable
fun SoloSlotLayoutPreview() {
    WelcomeToFlipTheme {
        SoloSlotLayout(
            drawStack = Card(action = Astronaut, number = Number12),
            discardStack = Card(action = Lightning, number = Number10),
            activeCards = listOf(
                Card(action = X, number = Number1),
                Card(action = Plant, number = Number2),
                Card(action = Water, number = Number3)), 
            astraCards = {
                Surface(color = Color.Gray, modifier = it) {}
            },
            modifier = Modifier
                .height(1200.dp)
                .padding(Dimen.spacingDouble))
    }
}