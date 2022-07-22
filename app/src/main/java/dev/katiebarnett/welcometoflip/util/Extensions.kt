package dev.katiebarnett.welcometoflip.util

fun <T>List<List<T>>.getStackSize(): Int? {
    return this.map { it.size }.distinct().minOrNull()
}

