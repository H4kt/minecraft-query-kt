package dev.h4kt.minecraftquery.types

import kotlinx.serialization.Serializable

@Serializable
data class FullServerInfo(
    val motd: String,
    val map: String?,
    val software: String?,
    val plugins: List<String>?,
    val onlinePlayers: Int,
    val maxPlayers: Int,
    val players: List<String>
)
