package dev.katiebarnett.welcometoflip.core.models

data class SavedGame(
    val position: Int,
    val gameType: GameType?,
    val seed: Long,
    val lastModified: Long
)