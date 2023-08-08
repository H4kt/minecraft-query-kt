package dev.h4kt.minecraftquery.engines.tcp

import dev.h4kt.minecraftquery.engines.QueryEngine
import dev.h4kt.minecraftquery.engines.tcp.packets.HandshakePacket
import dev.h4kt.minecraftquery.engines.tcp.packets.Packet
import dev.h4kt.minecraftquery.engines.tcp.packets.StatusRequestPacket
import dev.h4kt.minecraftquery.engines.tcp.responses.TCPStatusResponse
import dev.h4kt.minecraftquery.types.BasicServerInfo
import dev.h4kt.minecraftquery.types.FullServerInfo
import io.ktor.network.selector.*
import io.ktor.network.sockets.*
import io.ktor.util.cio.*
import io.ktor.utils.io.*
import kotlinx.coroutines.Dispatchers
import kotlinx.serialization.json.Json

class TCPQueryEngine(
    val protocolVersion: Int
) : QueryEngine {

    private val json = Json {
        isLenient = true
        ignoreUnknownKeys = true
    }

    override suspend fun queryBasicInfo(
        host: String,
        port: UShort
    ): BasicServerInfo {

        val response = getServerInfo(
            host = host,
            port = port,
            protocolVersion = protocolVersion
        )

        return BasicServerInfo(
            motd = response.description.text,
            map = null,
            onlinePlayers = response.players.online,
            maxPlayers = response.players.max
        )
    }

    override suspend fun queryFullInfo(
        host: String,
        port: UShort
    ): FullServerInfo {

        val response = getServerInfo(
            host = host,
            port = port,
            protocolVersion = protocolVersion
        )

        return FullServerInfo(
            motd = response.description.text,
            map = null,
            software = null,
            plugins = null,
            onlinePlayers = response.players.online,
            maxPlayers = response.players.max,
            players = response.players.sample.map { it.name }
        )
    }

    private suspend fun getServerInfo(
        host: String,
        port: UShort,
        protocolVersion: Int
    ): TCPStatusResponse {

        val selectorManager = SelectorManager(Dispatchers.IO)
        val socket = aSocket(selectorManager).tcp().connect(host, port.toInt())

        socket.openWriteChannel().use {

            writePacket(
                HandshakePacket(
                    protocolVersion = protocolVersion,
                    serverAddress = host,
                    serverPort = port,
                    nextState = HandshakePacket.NextState.STATUS
                )
            )

            writePacket(StatusRequestPacket)

        }

        val readChannel = socket.openReadChannel()
        readChannel.awaitContent()

        val data = readChannel.readUTF8Line()
            ?: throw IllegalStateException("No response from server")

        return json.decodeFromString(data)
    }

    private suspend fun ByteWriteChannel.writePacket(packet: Packet) {

        val serialized = packet.serialize()

        writeByte(serialized.size)
        writeFully(serialized)

    }

}
