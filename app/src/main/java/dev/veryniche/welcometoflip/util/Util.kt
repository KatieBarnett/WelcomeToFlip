package dev.veryniche.welcometoflip.util

import dev.veryniche.welcometoflip.core.models.Action
import dev.veryniche.welcometoflip.core.models.Card

fun List<Card>.transformCardsToMap(): Map<Action, Int> {
    return this
        .groupingBy { card -> card.action } // Group by the 'action' property of each Card
        .eachCount() // Count the occurrences of each group
        .filterKeys { it is Action } // Ensure the key is an Action (though it should be already if your data is consistent)
        .mapKeys { it.key as Action } // Cast the CardFace key back to Action
}
