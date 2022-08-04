package dev.katiebarnett.welcometoflip.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import dev.katiebarnett.welcometoflip.R
import dev.katiebarnett.welcometoflip.core.models.AstraA
import dev.katiebarnett.welcometoflip.core.models.Astronaut
import dev.katiebarnett.welcometoflip.core.models.CardFace
import dev.katiebarnett.welcometoflip.core.models.Number12
import dev.katiebarnett.welcometoflip.theme.Dimen
import dev.katiebarnett.welcometoflip.theme.WelcomeToFlipTheme

@Composable
fun CardFaceDisplay(
    cardFace: CardFace,
    peek: CardFace? = null,
    modifier: Modifier = Modifier
) {
    ConstraintLayout(
        modifier = modifier.fillMaxSize()
    ) {
        val (peekTop, peekBottom, image, background) = createRefs()
        
        // TODO fix alt text

        Surface(
            color = cardFace.backgroundColor ?: MaterialTheme.colorScheme.surface,
            modifier = Modifier
                .clip(RoundedCornerShape(Dimen.Card.radius))
                .border(
                    width = Dimen.Card.border,
                    color = MaterialTheme.colorScheme.outline,
                    shape = RoundedCornerShape(Dimen.Card.radius)
                )
                .constrainAs(background) {
                    top.linkTo(parent.top)
                    bottom.linkTo(parent.bottom)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                    width = Dimension.fillToConstraints
                    height = Dimension.fillToConstraints
                }) {}

        peek?.backgroundColor?.let { peekColor ->
            Image(
                painter = painterResource(R.drawable.peek_triangle),
                contentDescription = stringResource(id = R.string.action_deck_alt),
                colorFilter = ColorFilter.tint(peekColor),
                modifier = Modifier
                    .size(Dimen.Card.Peek.size)
                    .rotate(180f)
                    .constrainAs(peekTop) {
                        top.linkTo(parent.top, margin = Dimen.spacing)
                        start.linkTo(parent.start, margin = Dimen.spacing)
                    }
            )
        }
        Image(
            painter = painterResource(cardFace.drawableRes),
            contentDescription = stringResource(id = R.string.action_deck_alt),
            modifier = Modifier
                .constrainAs(image) {
                    top.linkTo(background.top)
                    bottom.linkTo(background.bottom)
                    start.linkTo(background.start)
                    end.linkTo(background.end)
                }
        )
        peek?.backgroundColor?.let { peekColor ->
            Image(
                painter = painterResource(R.drawable.peek_triangle),
                contentDescription = stringResource(id = R.string.action_deck_alt),
                colorFilter = ColorFilter.tint(peekColor),
                modifier = Modifier
                    .size(Dimen.Card.Peek.size)
                    .constrainAs(peekBottom) {
                        bottom.linkTo(parent.bottom, margin = Dimen.spacing)
                        end.linkTo(parent.end, margin = Dimen.spacing)
                    }

            )
        }
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

@Preview
@Composable
fun NumberCardPreview() {
    WelcomeToFlipTheme {
        CardFaceDisplay(Number12, Astronaut, modifier = Modifier.height(400.dp))
    }
}

@Preview
@Composable
fun ActionCardPreview() {
    WelcomeToFlipTheme {
        CardFaceDisplay(Astronaut, modifier = Modifier.height(400.dp))
    }
}

@Preview
@Composable
fun LetterCardPreview() {
    WelcomeToFlipTheme {
        CardFaceDisplay(AstraA, modifier = Modifier.height(400.dp))
    }
}

@Preview
@Composable
fun CardDisplayPlaceholderPreview() {
    WelcomeToFlipTheme {
        CardDisplayPlaceholder(modifier = Modifier.height(400.dp))
    }
}