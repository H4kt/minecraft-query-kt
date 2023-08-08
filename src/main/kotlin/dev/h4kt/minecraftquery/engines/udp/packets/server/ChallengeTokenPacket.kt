package dev.h4kt.minecraftquery.engines.udp.packets.server

import dev.h4kt.minecraftquery.extensions.readNullTerminatedString
import io.ktor.utils.io.*
import java.net.DatagramSocket

class ChallengeTokenPacket(
    override val type: Byte,
    override val sessionId: Int,
    val challengeToken: Int
) : ServerPacket() {

    companion object {

        suspend fun receive(
            socket: DatagramSocket
        ) = fromChannel(socket.receiveData(32))

        suspend fun fromChannel(
            channel: ByteReadChannel
        ) = ChallengeTokenPacket(
            type = channel.readByte(),
            sessionId = channel.readInt(),
            challengeToken = channel.readNullTerminatedString().toInt()
        )

    }

}
