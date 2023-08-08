package dev.h4kt.minecraftquery.types

data class BasicServerInfo(
    val motd: String,
    val map: String?,
    val onlinePlayers: Int,
    val maxPlayers: Int
)
