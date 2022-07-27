package dev.katiebarnett.welcometoflip.data

import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import dev.katiebarnett.welcometoflip.R
import dev.katiebarnett.welcometoflip.models.Card


sealed class GameType(
    @DrawableRes open val icon: Int,
    @StringRes open val displayName: Int,
    open val name: String
)

object WelcomeToTheMoon: GameType(
    name = "WelcomeToTheMoon",
    icon = R.drawable.noun_rocket_4925595,
    displayName = R.string.game_welcome_to_the_moon
)

sealed class CardFace(
    @DrawableRes open val drawableRes: Int,
    @ColorRes open val backgroundRes: Int,
)

sealed class Action(drawableRes: Int, backgroundRes: Int): CardFace(drawableRes, backgroundRes)

object Plant: Action(R.drawable.noun_plant_4982354, R.color.action_plant)
object Water: Action(R.drawable.noun_water_drop_1506321, R.color.action_water)
object Lightning: Action(R.drawable.noun_thunder_4978759, R.color.action_lightning)
object Robot: Action(R.drawable.noun_robot_4971447, R.color.action_robot)
object Astronaut: Action(R.drawable.noun_astronaut_2801977, R.color.action_astronaut)
object X: Action(R.drawable.noun_remove_schedule_4359088, R.color.action_x)


sealed class Number(drawableRes: Int): CardFace(drawableRes, R.color.card_background)

object Number1: Number(R.drawable.number_1)
object Number2: Number(R.drawable.number_2)
object Number3: Number(R.drawable.number_3)
object Number4: Number(R.drawable.number_4)
object Number5: Number(R.drawable.number_5)
object Number6: Number(R.drawable.number_6)
object Number7: Number(R.drawable.number_7)
object Number8: Number(R.drawable.number_8)
object Number9: Number(R.drawable.number_9)
object Number10: Number(R.drawable.number_10)
object Number11: Number(R.drawable.number_11)
object Number12: Number(R.drawable.number_12)
object Number13: Number(R.drawable.number_13)
object Number14: Number(R.drawable.number_14)
object Number15: Number(R.drawable.number_15)

fun String.mapToGameType() : GameType? {
    return when(this) {
        WelcomeToTheMoon.name -> WelcomeToTheMoon
        else -> null
    }
}

 val welcomeToTheMoonDeck = listOf(
     Card(Robot, Number1), 
     Card(Lightning, Number1),
     
     Card(Robot, Number2),
     Card(Plant, Number2),
     
     Card(X, Number3),
     Card(Astronaut, Number3),
     Card(Water, Number3),
     
     Card(X, Number4),
     Card(Astronaut, Number4),
     Card(Plant, Number4),
     Card(Lightning, Number4),
     
     Card(Robot, Number5),
     Card(Robot, Number5),
     Card(Lightning, Number5),
     Card(Lightning, Number5),
     Card(Plant, Number5),
     
     Card(X, Number6),
     Card(Astronaut, Number6),
     Card(Water, Number6),
     Card(Plant, Number6),
     Card(Lightning, Number6),
     Card(Robot, Number6),
     
     Card(Water, Number7),
     Card(Plant, Number7),
     Card(Plant, Number7),
     Card(Lightning, Number7),
     Card(Lightning, Number7),
     Card(Robot, Number7),
     
     Card(X, Number8),
     Card(Astronaut, Number8),
     Card(Water, Number8),
     Card(Plant, Number8),
     Card(Plant, Number8),
     Card(Robot, Number8),
     Card(Robot, Number8),
     
     Card(Water, Number9),
     Card(Plant, Number9),
     Card(Plant, Number9),
     Card(Lightning, Number9),
     Card(Lightning, Number9),
     Card(Robot, Number9),

     Card(X, Number10),
     Card(Astronaut, Number10),
     Card(Water, Number10),
     Card(Plant, Number10),
     Card(Lightning, Number10),
     Card(Robot, Number10),

     Card(Plant, Number11),
     Card(Lightning, Number11),
     Card(Lightning, Number11),
     Card(Robot, Number11),
     Card(Robot, Number11),

     Card(X, Number12),
     Card(Astronaut, Number12),
     Card(Plant, Number12),
     Card(Lightning, Number12),
     
     Card(X, Number13),
     Card(Astronaut, Number13),
     Card(Water, Number13),
     
     Card(Plant, Number14),
     Card(Robot, Number14),

     Card(Lightning, Number15),
     Card(Robot, Number15)
)
    