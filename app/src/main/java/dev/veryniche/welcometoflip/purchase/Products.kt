package dev.veryniche.welcometoflip.purchase

import androidx.annotation.DrawableRes
import dev.veryniche.welcometoflip.core.models.GameType
import dev.veryniche.welcometoflip.core.models.WelcomeToClassic
import dev.veryniche.welcometoflip.core.models.WelcomeToTheMoon
import dev.veryniche.welcometoflip.core.models.mapToGameType
import dev.veryniche.welcometoflip.purchase.Products.PRODUCT_PREFIX
import dev.veryniche.welcometoflip.purchase.Products.SOLO_SUFFIX

val multiplayerGameIds = listOf(
    WelcomeToTheMoon.mapToProductId(false),
)

val soloGameIds = listOf(
    WelcomeToClassic.mapToProductId(true),
    WelcomeToTheMoon.mapToProductId(true),
)

val appProductList =
    listOf(
        getProductQuery(Products.adRemoval),
        getProductQuery(Products.bundle),
    ) + multiplayerGameIds.map { getProductQuery(it) } + soloGameIds.map { getProductQuery(it) }

object Products {
    const val PRODUCT_PREFIX = "dev.veryniche.welcometoflip"
    const val SOLO_SUFFIX = "solo"

    const val adRemoval = "$PRODUCT_PREFIX.adremoval"

//    const val sync = "$PRODUCT_PREFIX.sync"
    const val bundle = "$PRODUCT_PREFIX.bundle"
}

fun GameType.mapToProductId(solo: Boolean): String {
    return if (solo) {
        "$PRODUCT_PREFIX.${name.lowercase()}.$SOLO_SUFFIX"
    } else {
        "$PRODUCT_PREFIX.${name.lowercase()}"
    }
}

@DrawableRes fun String.getLargeIcon(): Int? {
    return when (this) {
        // TODO add images for ad removal and bundle
        else -> {
            val gameName = this
                .replace("$PRODUCT_PREFIX.", "")
                .replace(".$SOLO_SUFFIX", "")
            gameName.mapToGameType()?.largeIcon?.let {
                return it
            }
        }
    }
}
