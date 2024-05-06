package dev.veryniche.welcometoflip.util

import dev.veryniche.welcometoflip.purchase.InAppProduct
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter

fun <T>List<List<T>>.getStackSize(): Int? {
    return this.map { it.size }.distinct().minOrNull()
}

fun Long.displayDateTime(dateTimeFormat: String): String {
    val dateTime = LocalDateTime.ofInstant(Instant.ofEpochMilli(this), ZoneId.systemDefault())
    return dateTime.format(DateTimeFormatter.ofPattern(dateTimeFormat))
}

fun Map<String, InAppProduct>.isAvailablePurchases() = any { it.value.purchased != true }

