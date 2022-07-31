package dev.katiebarnett.welcometoflip.core.models

data class SavedGame(
    val position: Int,
    val gameType: String,
    val seed: Long,
    val dateModified: Long
)