package dev.h4kt.minecraftquery.engines.udp.packets.client

import java.io.OutputStream

internal class HandshakePacket(
    sessionId: Int
) : ClientPacket(0x09, sessionId) {

    override fun OutputStream.writeData() {
        // Nothing to do here, packet is empty
    }

}
