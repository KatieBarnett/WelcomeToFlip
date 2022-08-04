package dev.katiebarnett.welcometoflip.core.models

import androidx.annotation.DrawableRes
import androidx.compose.ui.graphics.Color

sealed class CardFace(
    @DrawableRes open val drawableRes: Int,
    open val backgroundColor: Color?,
)

sealed class Action(
    drawableRes: Int,
    backgroundColor: Color
): CardFace(drawableRes, backgroundColor)

sealed class Number(drawableRes: Int): CardFace(drawableRes, null)

sealed class Letter(
    drawableRes: Int,
    backgroundColor: Color
): CardFace(drawableRes, backgroundColor)



 
    