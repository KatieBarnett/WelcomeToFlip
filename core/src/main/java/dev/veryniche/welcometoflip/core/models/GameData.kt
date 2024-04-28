package dev.veryniche.welcometoflip.core.models

import dev.veryniche.welcometoflip.core.R
import dev.veryniche.welcometoflip.core.theme.*

val WelcomeToClassic = GameType(
    name = "WelcomeToClassic",
    icon = R.drawable.noun_rocket_4925595,
    largeIcon = R.drawable.noun_moon_6086589,
    displayName = R.string.game_welcome_to_classic,
    solo = false,
)

val WelcomeToTheMoon = GameType(
    name = "WelcomeToTheMoonSolo",
    icon = R.drawable.noun_rocket_4925595,
    largeIcon = R.drawable.noun_moon_6086589,
    displayName = R.string.game_welcome_to_the_moon,
    solo = false,
)

data object Plant : Action(R.drawable.noun_plant_4982354, ActionPlant)
data object Water : Action(R.drawable.noun_water_drop_1506321, ActionWater)
data object Lightning : Action(R.drawable.noun_thunder_4978759, ActionLightning)
data object Robot : Action(R.drawable.noun_robot_4971447, ActionRobot)
data object Astronaut : Action(R.drawable.noun_astronaut_2801977, ActionAstronaut)
data object X : Action(R.drawable.noun_remove_schedule_4359088, ActionX)

data object AstraA : Letter(R.drawable.letter_a, DuskyPink)
data object AstraB : Letter(R.drawable.letter_b, DuskyPurple)
data object AstraC : Letter(R.drawable.letter_c, DuskyYellow)

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

val welcomeToTheMoonSoloEffectCards = listOf(
    Card(AstraA, AstraA),
    Card(AstraB, AstraB),
    Card(AstraC, AstraC)
)

val welcomeToTheMoonAvailableActions = listOf(
    Plant,
    Water,
    Lightning,
    Robot,
    Astronaut,
    X
)
