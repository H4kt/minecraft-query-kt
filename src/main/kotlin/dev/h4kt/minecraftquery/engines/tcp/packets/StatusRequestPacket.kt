package dev.h4kt.minecraftquery.engines.tcp.packets

import java.io.OutputStream

internal object StatusRequestPacket : Packet(0x00) {

    override fun OutputStream.writeData() {
        // Nothing to do here, packet is empty
    }

}
