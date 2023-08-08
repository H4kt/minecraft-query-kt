package dev.h4kt.minecraftquery.engines.udp.packets.server

import dev.h4kt.minecraftquery.extensions.readNullTerminatedString
import io.ktor.utils.io.*
import java.net.DatagramSocket

class FullInfoPacket(
    override val type: Byte,
    override val sessionId: Int,
    val motd: String,
    val gameType: String,
    val gameId: String,
    val version: String,
    val software: String,
    val plugins: List<String>,
    val map: String,
    val onlinePlayers: Int,
    val maxPlayers: Int,
    val port: UShort,
    val ip: String,
    val players: List<String>
) : ServerPacket() {

    companion object {

        const val KEY_MOTD = "hostname"
        const val KEY_GAME_TYPE = "gametype"
        const val KEY_GAME_ID = "game_id"
        const val KEY_VERSION = "version"
        const val KEY_PLUGINS = "plugins"
        const val KEY_MAP = "map"
        const val KEY_ONLINE_PLAYERS = "numplayers"
        const val KEY_MAX_PLAYERS = "maxplayers"
        const val KEY_PORT = "hostport"
        const val KEY_IP = "hostip"

        const val VANILLA_SOFTWARE = "Vanilla"

        suspend fun receive(
            socket: DatagramSocket
        ) = fromChannel(socket.receiveData(1024))

        suspend fun fromChannel(
            channel: ByteReadChannel
        ): FullInfoPacket {

            val type = channel.readByte()
            val sessionId = channel.readInt()

            repeat(11) {
                channel.readByte() // Skipping padding
            }

            val payload = mutableMapOf<String, String>()
            while (true) {

                val key = channel.readNullTerminatedString()
                if (key.isEmpty()) {
                    break
                }

                payload[key] = channel.readNullTerminatedString()

            }

            val pluginsInfo = payload[KEY_PLUGINS]!!
                .split(": ")

            val software = pluginsInfo.getOrNull(0)
                ?: VANILLA_SOFTWARE

            val plugins = pluginsInfo
                .getOrNull(1)
                ?.split("; ")
                ?: emptyList()

            repeat(10) {
                channel.readByte() // Skipping padding
            }

            val players = mutableListOf<String>()
            while (true) {

                val player = channel.readNullTerminatedString()
                if (player.isEmpty()) {
                    break
                }

                players += player

            }

            return FullInfoPacket(
                type = type,
                sessionId = sessionId,
                motd = payload[KEY_MOTD]!!,
                gameType = payload[KEY_GAME_TYPE]!!,
                gameId = payload[KEY_GAME_ID]!!,
                version = payload[KEY_VERSION]!!,
                software = software,
                plugins = plugins,
                map = payload[KEY_MAP]!!,
                onlinePlayers = payload[KEY_ONLINE_PLAYERS]!!.toInt(),
                maxPlayers = payload[KEY_MAX_PLAYERS]!!.toInt(),
                port = payload[KEY_PORT]!!.toUShort(),
                ip = payload[KEY_IP]!!,
                players = players
            )
        }

    }

}
