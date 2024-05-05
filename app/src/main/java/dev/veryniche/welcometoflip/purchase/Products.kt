package dev.veryniche.welcometoflip.purchase

import dev.veryniche.welcometoflip.core.models.GameType
import dev.veryniche.welcometoflip.core.models.WelcomeToClassic
import dev.veryniche.welcometoflip.core.models.WelcomeToTheMoon
import dev.veryniche.welcometoflip.purchase.Products.PRODUCT_PREFIX
import dev.veryniche.welcometoflip.purchase.Products.SOLO_SUFFIX

val appProductList =
    listOf(
        getProductQuery(Products.adRemoval),
        getProductQuery(Products.bundle),
        getProductQuery(WelcomeToClassic.mapToProductId(true)),
        getProductQuery(WelcomeToTheMoon.mapToProductId(false)),
        getProductQuery(WelcomeToTheMoon.mapToProductId(true)),
    )

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

data class PurchaseStatus(
    val productId: String,
    val purchased: Boolean,
    val purchasePrice: String,
)
