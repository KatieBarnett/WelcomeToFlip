package dev.veryniche.welcometoflip.core.models

import dev.veryniche.welcometoflip.core.BuildConfig
import dev.veryniche.welcometoflip.core.R
import dev.veryniche.welcometoflip.core.theme.*

val WelcomeToClassic = GameType(
    name = "WelcomeToClassic",
    icon = R.drawable.ic_sign,
    largeIcon = R.drawable.noun_house_6786764,
    displayName = R.string.game_welcome_to_classic,
    actionCardName = R.string.game_welcome_to_classic_action_card_name,
    soloAvailable = true,
    soloPurchased = BuildConfig.DEBUG,
    purchased = true // Multiplayer purchased by default
)

val WelcomeToTheMoon = GameType(
    name = "WelcomeToTheMoon",
    icon = R.drawable.noun_rocket_4925595,
    largeIcon = R.drawable.noun_moon_6086589,
    displayName = R.string.game_welcome_to_the_moon,
    actionCardName = R.string.game_welcome_to_the_moon_action_card_name,
    soloAvailable = true,
    soloPurchased = BuildConfig.DEBUG,
    purchased = BuildConfig.DEBUG
)

// Welcome To Classic
data object Fence : Action(R.drawable.noun_fence_6826093, ActionFence)
data object Improve : Action(R.drawable.noun_increase_6785191, ActionImprove)
data object Park : Action(R.drawable.noun_park_6721083, ActionPark)
data object Pool : Action(R.drawable.noun_pool_5817216, ActionPool)
data object Worker : Action(R.drawable.noun_traffic_cone_57855, ActionWorker)
data object Extension : Action(R.drawable.noun_mailbox_6180823, ActionExtension)

// Welcome To the Moon
data object Plant : Action(R.drawable.noun_plant_4982354, ActionPlant)
data object Water : Action(R.drawable.noun_water_drop_1506321, ActionWater)
data object Lightning : Action(R.drawable.noun_thunder_4978759, ActionLightning)
data object Robot : Action(R.drawable.noun_robot_4971447, ActionRobot)
data object Astronaut : Action(R.drawable.noun_astronaut_2801977, ActionAstronaut)
data object X : Action(R.drawable.noun_remove_schedule_4359088, ActionX)

data object SoloA : Letter(R.drawable.letter_a, DuskyPink)
data object SoloB : Letter(R.drawable.letter_b, DuskyPurple)
data object SoloC : Letter(R.drawable.letter_c, DuskyYellow)

val welcomeToClassicDeck = listOf(
    Card(Improve, Number1),
    Card(Fence, Number1),

    Card(Park, Number2),
    Card(Fence, Number2),

    Card(Pool, Number3),
    Card(Extension, Number3),
    Card(Worker, Number3),

    Card(Improve, Number4),
    Card(Park, Number4),
    Card(Worker, Number4),
    Card(Extension, Number4),

    Card(Improve, Number5),
    Card(Park, Number5),
    Card(Improve, Number5),
    Card(Fence, Number5),
    Card(Fence, Number5),

    Card(Fence, Number6),
    Card(Park, Number6),
    Card(Extension, Number6),
    Card(Worker, Number6),
    Card(Pool, Number6),
    Card(Improve, Number6),

    Card(Improve, Number7),
    Card(Improve, Number7),
    Card(Pool, Number7),
    Card(Park, Number7),
    Card(Park, Number7),
    Card(Fence, Number7),

    Card(Fence, Number8),
    Card(Extension, Number8),
    Card(Worker, Number8),
    Card(Park, Number8),
    Card(Park, Number8),
    Card(Fence, Number8),
    Card(Pool, Number8),

    Card(Improve, Number9),
    Card(Park, Number9),
    Card(Park, Number9),
    Card(Pool, Number9),
    Card(Fence, Number9),
    Card(Improve, Number9),

    Card(Improve, Number10),
    Card(Park, Number10),
    Card(Pool, Number10),
    Card(Extension, Number10),
    Card(Worker, Number10),
    Card(Fence, Number10),

    Card(Improve, Number11),
    Card(Fence, Number11),
    Card(Park, Number11),
    Card(Improve, Number11),
    Card(Fence, Number11),

    Card(Improve, Number12),
    Card(Worker, Number12),
    Card(Extension, Number12),
    Card(Park, Number12),

    Card(Extension, Number13),
    Card(Pool, Number13),
    Card(Worker, Number13),

    Card(Park, Number14),
    Card(Fence, Number14),

    Card(Improve, Number15),
    Card(Fence, Number15)
)

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

val welcomeToClassicSoloEffectCards = listOf(
    Card(SoloA, SoloA),
    Card(SoloB, SoloB),
    Card(SoloC, SoloC)
)

val welcomeToTheMoonSoloEffectCards = listOf(
    Card(SoloA, SoloA),
    Card(SoloB, SoloB),
    Card(SoloC, SoloC)
)

val welcomeToTheMoonAvailableActions = listOf(
    Plant,
    Water,
    Lightning,
    Robot,
    Astronaut,
    X
)
