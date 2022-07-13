package dev.katiebarnett.welcometoflip.data

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import dev.katiebarnett.welcometoflip.R
import dev.katiebarnett.welcometoflip.models.Card


sealed class GameType(
    @DrawableRes open val icon: Int,
    @StringRes open val name: Int
)

object WelcomeToTheMoon: GameType(icon = R.drawable.noun_rocket_4925595, name = R.string.game_welcome_to_the_moon)

 val welcomeToTheMoonDeck = listOf(
    Card(action = R.drawable.action_robot, number = R.drawable.action_robot),
    Card(R.drawable.action_robot, R.drawable.action_robot),
    Card(R.drawable.action_robot, R.drawable.action_robot),
    Card(R.drawable.action_robot, R.drawable.action_robot)
)
    