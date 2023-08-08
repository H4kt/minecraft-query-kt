package dev.h4kt.minecraftquery.engines.udp.packets.server

import io.ktor.utils.io.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.net.DatagramPacket
import java.net.DatagramSocket

abstract class ServerPacket {

    companion object {

        @JvmStatic
        protected suspend fun DatagramSocket.receiveData(size: Int): ByteReadChannel {

            val buffer = ByteArray(size)
            val packet = DatagramPacket(buffer, size)

            withContext(Dispatchers.IO) {
                receive(packet)
            }

            return ByteReadChannel(packet.data, offset = 0, length = packet.length)
        }

    }

    abstract val type: Byte
    abstract val sessionId: Int

}
