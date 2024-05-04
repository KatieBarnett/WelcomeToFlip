package dev.veryniche.welcometoflip.previews

import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.unit.dp
import dev.veryniche.welcometoflip.components.CardFaceDisplay
import dev.veryniche.welcometoflip.core.models.Action
import dev.veryniche.welcometoflip.core.models.Astronaut
import dev.veryniche.welcometoflip.core.models.Extension
import dev.veryniche.welcometoflip.core.models.Fence
import dev.veryniche.welcometoflip.core.models.Improve
import dev.veryniche.welcometoflip.core.models.Lightning
import dev.veryniche.welcometoflip.core.models.Number
import dev.veryniche.welcometoflip.core.models.Number1
import dev.veryniche.welcometoflip.core.models.Number10
import dev.veryniche.welcometoflip.core.models.Number11
import dev.veryniche.welcometoflip.core.models.Number12
import dev.veryniche.welcometoflip.core.models.Number13
import dev.veryniche.welcometoflip.core.models.Number14
import dev.veryniche.welcometoflip.core.models.Number15
import dev.veryniche.welcometoflip.core.models.Number2
import dev.veryniche.welcometoflip.core.models.Number3
import dev.veryniche.welcometoflip.core.models.Number4
import dev.veryniche.welcometoflip.core.models.Number5
import dev.veryniche.welcometoflip.core.models.Number6
import dev.veryniche.welcometoflip.core.models.Number7
import dev.veryniche.welcometoflip.core.models.Number8
import dev.veryniche.welcometoflip.core.models.Number9
import dev.veryniche.welcometoflip.core.models.Park
import dev.veryniche.welcometoflip.core.models.Plant
import dev.veryniche.welcometoflip.core.models.Pool
import dev.veryniche.welcometoflip.core.models.Robot
import dev.veryniche.welcometoflip.core.models.Water
import dev.veryniche.welcometoflip.core.models.Worker
import dev.veryniche.welcometoflip.core.models.X
import dev.veryniche.welcometoflip.theme.Dimen
import dev.veryniche.welcometoflip.theme.WelcomeToFlipTheme

class NumberCardPreviewParameterProvider : PreviewParameterProvider<Number> {
    override val values: Sequence<Number> = sequenceOf(
        Number1, Number2, Number3, Number4, Number5, Number6, Number7, Number8, Number9,
        Number10, Number11, Number12, Number13, Number14, Number15
    )
}

@Preview(group = "Number Cards", showBackground = true)
@Composable
fun NumberCardPreview(
    @PreviewParameter(NumberCardPreviewParameterProvider::class) data: Number
) {
    WelcomeToFlipTheme {
        CardFaceDisplay(
            data,
            Astronaut,
            modifier = Modifier
                .padding(Dimen.spacing)
                .height(400.dp)
                .width(300.dp)
        )
    }
}

class WelcomeToClassicActionCardPreviewParameterProvider : PreviewParameterProvider<Action> {
    override val values: Sequence<Action> = sequenceOf(
        Fence,
        Improve,
        Park,
        Pool,
        Worker,
        Extension
    )
}

@Preview(group = "Welcome To Classic Cards", showBackground = true)
@Composable
fun WelcomeToClassicActionCardPreview(
    @PreviewParameter(WelcomeToClassicActionCardPreviewParameterProvider::class) data: Action
) {
    WelcomeToFlipTheme {
        CardFaceDisplay(
            data,
            modifier = Modifier
                .padding(Dimen.spacing)
                .height(400.dp)
                .width(300.dp)
        )
    }
}

class WelcomeToTheMoonActionCardPreviewParameterProvider : PreviewParameterProvider<Action> {
    override val values: Sequence<Action> = sequenceOf(
        Plant,
        Water,
        Lightning,
        Robot,
        Astronaut,
        X
    )
}

@Preview(group = "Welcome To The Moon Cards", showBackground = true)
@Composable
fun WelcomeToTheMoonActionCardPreview(
    @PreviewParameter(WelcomeToTheMoonActionCardPreviewParameterProvider::class) data: Action
) {
    WelcomeToFlipTheme {
        CardFaceDisplay(
            data,
            modifier = Modifier
                .padding(Dimen.spacing)
                .height(400.dp)
                .width(300.dp)
        )
    }
}
