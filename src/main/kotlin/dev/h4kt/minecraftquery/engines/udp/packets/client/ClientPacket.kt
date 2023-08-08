package dev.h4kt.minecraftquery.engines.udp.packets.client

import dev.h4kt.minecraftquery.extensions.writeIntBigEndian
import dev.h4kt.minecraftquery.extensions.writeUShortBigEndian
import java.io.ByteArrayOutputStream
import java.io.OutputStream

internal abstract class ClientPacket(
    val id: Int,
    val sessionId: Int
) {

    abstract fun OutputStream.writeData()

    fun serialize(): ByteArray {

        val stream = ByteArrayOutputStream().apply {
            writeUShortBigEndian(0xFEFD.toUShort())
            write(id)
            writeIntBigEndian(sessionId)
            writeData()
        }

        return stream.toByteArray()
    }

}
