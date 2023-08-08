package dev.h4kt.minecraftquery.engines.udp

import dev.h4kt.minecraftquery.engines.QueryEngine
import dev.h4kt.minecraftquery.engines.udp.packets.client.ClientPacket
import dev.h4kt.minecraftquery.engines.udp.packets.client.HandshakePacket
import dev.h4kt.minecraftquery.engines.udp.packets.client.StatusRequestPacket
import dev.h4kt.minecraftquery.engines.udp.packets.server.BasicInfoPacket
import dev.h4kt.minecraftquery.engines.udp.packets.server.ChallengeTokenPacket
import dev.h4kt.minecraftquery.engines.udp.packets.server.FullInfoPacket
import dev.h4kt.minecraftquery.types.BasicServerInfo
import dev.h4kt.minecraftquery.types.FullServerInfo
import io.ktor.utils.io.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.net.DatagramPacket
import java.net.DatagramSocket
import java.net.InetSocketAddress
import kotlin.time.Duration
import kotlin.time.Duration.Companion.seconds

class UDPQueryEngine(
    val timeout: Duration = 1.seconds,
    val sessionIdFactory: () -> Int = { (0..255).random() }
) : QueryEngine {

    companion object {
        const val MINECRAFT_SESSION_ID_MASK = 0x0F0F0F0F
    }

    override suspend fun queryBasicInfo(
        host: String,
        port: UShort
    ): BasicServerInfo {

        val socket = initializeQuery(
            host = host,
            port = port,
            isFullInfo = false
        )

        val infoPacket = BasicInfoPacket.receive(socket)

        return BasicServerInfo(
            motd = infoPacket.motd,
            map = infoPacket.map,
            onlinePlayers = infoPacket.onlinePlayers,
            maxPlayers = infoPacket.maxPlayers
        )
    }

    override suspend fun queryFullInfo(
        host: String,
        port: UShort
    ): FullServerInfo {

        val socket = initializeQuery(
            host = host,
            port = port,
            isFullInfo = true
        )

        val infoPacket = FullInfoPacket.receive(socket)

        return FullServerInfo(
            motd = infoPacket.motd,
            map = infoPacket.map,
            software = infoPacket.software,
            plugins = infoPacket.plugins,
            onlinePlayers = infoPacket.onlinePlayers,
            maxPlayers = infoPacket.maxPlayers,
            players = infoPacket.players
        )
    }

    private suspend fun initializeQuery(
        host: String,
        port: UShort,
        isFullInfo: Boolean
    ): DatagramSocket {

        val socket = withContext(Dispatchers.IO) {
            DatagramSocket().apply {
                soTimeout = timeout.inWholeMilliseconds.toInt()
                connect(InetSocketAddress(host, port.toInt()))
            }
        }

        val sessionId = sessionIdFactory() and MINECRAFT_SESSION_ID_MASK
        socket.sendPacket(HandshakePacket(sessionId))

        val challengeTokenPacket = ChallengeTokenPacket.receive(socket)

        println("sessionId=$sessionId (${sessionId.toString(16)})")
        println("challengeToken=${challengeTokenPacket.challengeToken} (${challengeTokenPacket.challengeToken.toString(16)})")

        socket.sendPacket(
            StatusRequestPacket(
                sessionId = sessionId,
                challengeToken = challengeTokenPacket.challengeToken,
                isFullInfo = isFullInfo
            )
        )

        return socket
    }

    private suspend fun DatagramSocket.sendPacket(packet: ClientPacket) {

        val serialized = packet.serialize()

        val datagramPacket = DatagramPacket(
            serialized,
            serialized.size
        )

        withContext(Dispatchers.IO) {
            try {
                send(datagramPacket)
            } catch (ex: Exception) {
                ex.printStack()
            }
        }

    }

}
