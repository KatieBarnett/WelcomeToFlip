package dev.katiebarnett.welcometoflip.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
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
                    .fillMaxHeight(0.25f)
                    .aspectRatio(1f)
                    .rotate(180f)
                    .constrainAs(peekTop) {
                        top.linkTo(background.top, margin = Dimen.spacing)
                        start.linkTo(background.start, margin = Dimen.spacing)
                    }
            )
        }
        Image(
            painter = painterResource(cardFace.drawableRes),
            contentDescription = stringResource(id = R.string.action_deck_alt),
            modifier = Modifier
                .fillMaxHeight(0.5f)
                .fillMaxWidth(0.6f)
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
                    .fillMaxHeight(0.25f)
                    .aspectRatio(1f)
                    .constrainAs(peekBottom) {
                        bottom.linkTo(background.bottom, margin = Dimen.spacing)
                        end.linkTo(background.end, margin = Dimen.spacing)
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
        CardFaceDisplay(Number12, Astronaut, modifier = Modifier.height(400.dp).width(300.dp))
    }
}

@Preview
@Composable
fun ActionCardPreview() {
    WelcomeToFlipTheme {
        CardFaceDisplay(Astronaut, modifier = Modifier.height(400.dp).width(300.dp))
    }
}

@Preview
@Composable
fun LetterCardPreview() {
    WelcomeToFlipTheme {
        CardFaceDisplay(AstraA, modifier = Modifier.height(400.dp).width(300.dp))
    }
}

@Preview
@Composable
fun CardDisplayPlaceholderPreview() {
    WelcomeToFlipTheme {
        CardDisplayPlaceholder(modifier = Modifier.height(400.dp).width(300.dp))
    }
}