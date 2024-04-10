package dev.veryniche.welcometoflip.purchase

data class PurchaseStatus(
    val isAdRemovalPurchased: Boolean,
    val isSyncPurchased: Boolean,
    val isProBundlePurchased: Boolean,
    val isGameWelcomeToTheMoonPurchased: Boolean,
)

enum class PurchaseAction {
    AD_REMOVAL, SYNC, PRO_BUNDLE, GAME_WELCOME_TO_THE_MOON
}
