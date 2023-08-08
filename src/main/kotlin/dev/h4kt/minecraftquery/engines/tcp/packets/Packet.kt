package dev.h4kt.minecraftquery.engines.tcp.packets

import java.io.ByteArrayOutputStream
import java.io.OutputStream

internal abstract class Packet(
    val id: Int
) {

    abstract fun OutputStream.writeData()

    fun serialize(): ByteArray {

        val stream = ByteArrayOutputStream().apply {
            write(id)
            writeData()
        }

        return stream.toByteArray()
    }

}
