package dev.h4kt.minecraftquery.engines.tcp.packets.client

import java.io.OutputStream

/**
 * Server list ping status request packet
 *
 * [Reference](https://wiki.vg/Server_List_Ping#Status_Request)
 */
internal object StatusRequestPacket : Packet(0x00) {

    override fun OutputStream.writeData() {
        // Nothing to do here, packet is empty
    }

}
