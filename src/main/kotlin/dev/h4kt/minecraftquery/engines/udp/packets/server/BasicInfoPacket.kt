package dev.h4kt.minecraftquery.engines.udp.packets.server

import dev.h4kt.minecraftquery.extensions.readNullTerminatedString
import io.ktor.utils.io.*
import java.net.DatagramSocket

class BasicInfoPacket(
    override val type: Byte,
    override val sessionId: Int,
    val motd: String,
    val gameType: String,
    val map: String,
    val onlinePlayers: Int,
    val maxPlayers: Int,
    val port: UShort,
    val ip: String
) : ServerPacket() {

    companion object {

        suspend fun receive(
            socket: DatagramSocket
        ) = fromChannel(socket.receiveData(1024))

        suspend fun fromChannel(
            channel: ByteReadChannel
        ) = BasicInfoPacket(
            type = channel.readByte(),
            sessionId = channel.readInt(),
            motd = channel.readNullTerminatedString(),
            gameType = channel.readNullTerminatedString(),
            map = channel.readNullTerminatedString(),
            onlinePlayers = channel.readNullTerminatedString().toInt(),
            maxPlayers = channel.readNullTerminatedString().toInt(),
            port = channel.readShortLittleEndian().toUShort(),
            ip = channel.readNullTerminatedString()
        )

    }

}
