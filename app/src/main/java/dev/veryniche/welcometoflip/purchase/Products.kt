package dev.veryniche.welcometoflip.purchase

import dev.veryniche.welcometoflip.core.models.WelcomeToClassic
import dev.veryniche.welcometoflip.core.models.WelcomeToTheMoon

object Products {
    const val adRemoval = "dev.veryniche.welcometoflip.addremoval"
    const val sync = "dev.veryniche.welcometoflip.sync"
    const val bundle = "dev.veryniche.welcometoflip.bundle"
    val gameWelcomeToClassicSolo = "dev.veryniche.welcometoflip." + WelcomeToClassic.name + ".solo"
    val gameWelcomeToTheMoon = "dev.veryniche.welcometoflip." + WelcomeToTheMoon.name
    val gameWelcomeToTheMoonSolo = "dev.veryniche.welcometoflip." + WelcomeToTheMoon.name + ".solo"
}

enum class PurchaseAction {
    AD_REMOVAL, SYNC, PRO_BUNDLE, GAME_WELCOME_TO_THE_MOON
}

data class PurchaseStatus(
    val productId: String,
    val purchased: Boolean,
    val purchasePrice: String,
)
