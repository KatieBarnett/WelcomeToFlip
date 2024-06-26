package dev.veryniche.welcometoflip.core.models

data class SavedGame(
    val position: Int,
    val stackSize: Int,
    val gameType: GameType?,
    val seed: Long,
    val solo: Boolean,
    val lastModified: Long
) {
    val displayPosition: Int
        get() = position + 1
}