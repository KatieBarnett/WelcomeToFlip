package dev.katiebarnett.welcometoflip.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import dev.katiebarnett.welcometoflip.R
import dev.katiebarnett.welcometoflip.core.models.AstraA
import dev.katiebarnett.welcometoflip.core.models.Astronaut
import dev.katiebarnett.welcometoflip.core.models.CardFace
import dev.katiebarnett.welcometoflip.core.models.Number12
import dev.katiebarnett.welcometoflip.theme.Dimen
import dev.katiebarnett.welcometoflip.theme.WelcomeToFlipTheme

@Composable
fun CardFaceDisplay(
    cardFace: CardFace?,
    peek: CardFace? = null,
    modifier: Modifier = Modifier
) {
    if (cardFace != null) {
        Box(contentAlignment = Alignment.Center,
            modifier = modifier.fillMaxSize()
            .clip(RoundedCornerShape(Dimen.Card.radius))
            .border(
                Dimen.Card.border,
                MaterialTheme.colorScheme.primary,
                RoundedCornerShape(Dimen.Card.radius)
            )
            .background(cardFace.backgroundColor ?: MaterialTheme.colorScheme.surface)) {
            if (peek?.backgroundColor != null) {
                CardNumberContent(cardFace = cardFace, peek.backgroundColor ?: Color.Black)
            } else {
                CardActionContent(cardFace = cardFace)
            }
        }
    } else {
        CardDisplayPlaceholder(modifier)

    }}

@Composable
private fun CardNumberContent(
    cardFace: CardFace,
    peekColor: Color,
    modifier: Modifier = Modifier
) {
    Box(modifier = modifier
        .fillMaxSize()
        .padding(paddingValues = PaddingValues(Dimen.spacing, Dimen.spacing))) {
        Image(
            painter = painterResource(cardFace.drawableRes),
            contentDescription = null,
            modifier = modifier
                .fillMaxHeight(0.5f)
                .fillMaxWidth(0.6f)
                .align(Alignment.Center)
        )
        Image(
            painter = painterResource(R.drawable.peek_triangle),
            contentDescription = stringResource(id = R.string.action_deck_alt),
            colorFilter = ColorFilter.tint(peekColor),
            modifier = Modifier
                .fillMaxHeight(0.25f)
                .aspectRatio(1f)
                .rotate(180f)
                .align(Alignment.TopStart)
        )
        Image(
            painter = painterResource(R.drawable.peek_triangle),
            contentDescription = stringResource(id = R.string.action_deck_alt),
            colorFilter = ColorFilter.tint(peekColor),
            modifier = Modifier
                .fillMaxHeight(0.25f)
                .aspectRatio(1f)
                .align(Alignment.BottomEnd)
        )
    }
}

@Composable
private fun CardActionContent(
    cardFace: CardFace,
    modifier: Modifier = Modifier
) {
    Box(modifier = modifier
        .fillMaxSize()
        .padding(Dimen.spacing)) {
        Image(
            painter = painterResource(cardFace.drawableRes),
            contentDescription = null,
            modifier = modifier
                .fillMaxHeight(0.5f)
                .fillMaxWidth(0.6f)
                .align(Alignment.Center)
        )
    }
}

@Composable
fun CardDisplayPlaceholder(modifier: Modifier = Modifier) {
//    Box(
//        modifier = modifier
//            .fillMaxSize()
//            .clip(RoundedCornerShape(Dimen.Card.radius))
//            .background(MaterialTheme.colorScheme.surface),
//        contentAlignment = Alignment.Center
//    ) {
//        Text(text = stringResource(id = R.string.empty))
//    }
}

@Preview(group = "Card Components", showBackground = true)
@Composable
fun NumberCardPreview() {
    WelcomeToFlipTheme {
        CardFaceDisplay(Number12, Astronaut, modifier = Modifier
            .padding(Dimen.spacing)
            .height(400.dp)
            .width(300.dp))
    }
}

@Preview(group = "Card Components", showBackground = true)
@Composable
fun ActionCardPreview() {
    WelcomeToFlipTheme {
        CardFaceDisplay(Astronaut, modifier = Modifier
            .padding(Dimen.spacing)
            .height(400.dp)
            .width(300.dp))
    }
}

@Preview(group = "Card Components", showBackground = true)
@Composable
fun LetterCardPreview() {
    WelcomeToFlipTheme {
        CardFaceDisplay(AstraA, modifier = Modifier
            .padding(Dimen.spacing)
            .height(400.dp)
            .width(300.dp))
    }
}

@Preview(group = "Card Components", showBackground = true)
@Composable
fun CardDisplayPlaceholderPreview() {
    WelcomeToFlipTheme {
        CardDisplayPlaceholder(modifier = Modifier
            .padding(Dimen.spacing)
            .height(400.dp)
            .width(300.dp))
    }
}