package dev.h4kt.minecraftquery.engines.udp.packets.client

import dev.h4kt.minecraftquery.extensions.writeIntBigEndian
import java.io.OutputStream

internal class StatusRequestPacket(
    sessionId: Int,
    val challengeToken: Int,
    val isFullInfo: Boolean
) : ClientPacket(0x00, sessionId) {

    override fun OutputStream.writeData() {

        writeIntBigEndian(challengeToken)

        if (!isFullInfo) {
            return
        }

        repeat(4) {
            write(0x00)
        }

    }

}
