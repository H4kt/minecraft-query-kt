package dev.h4kt.minecraftquery.engines.tcp.packets

import dev.h4kt.minecraftquery.extensions.writeUShortLittleEndian
import dev.h4kt.minecraftquery.extensions.writeVarInt
import java.io.OutputStream

internal data class HandshakePacket(
    val protocolVersion: Int,
    val serverAddress: String,
    val serverPort: UShort,
    val nextState: NextState
) : Packet(0x00) {

    enum class NextState(
        val intValue: Int
    ) {
        STATUS(1), LOGIN(2)
    }

    override fun OutputStream.writeData() {

        writeVarInt(protocolVersion)

        writeVarInt(serverAddress.length)
        write(serverAddress.toByteArray())

        writeUShortLittleEndian(serverPort)

        writeVarInt(nextState.intValue)

    }

}
