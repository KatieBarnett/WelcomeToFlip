package dev.katiebarnett.welcometoflip.purchase

import dev.veryniche.welcometoflip.purchase.Products

fun purchasePro(manager: PurchaseManager,
                onError: (message: Int) -> Unit
) {
    manager.purchase(Products.proVersion, onError)
}

fun isProPurchased(purchasedProducts: List<String>) = purchasedProducts.contains(Products.proVersion)