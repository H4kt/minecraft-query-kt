package dev.h4kt.minecraftquery.engines.tcp.packets.server

import dev.h4kt.minecraftquery.serializers.SerialUUID
import kotlinx.serialization.Serializable

/**
 * Server list ping status response packet
 *
 * [Reference](https://wiki.vg/Server_List_Ping#Status_Response)
 */
@Serializable
internal data class StatusResponsePacket(
    val version: Version,
    val players: Players,
    val description: Description,
    val favicon: String,
    val enforcesSecureChat: Boolean? = false,
    val previewsChat: Boolean? = false
) {

    @Serializable
    data class Version(
        val name: String,
        val protocol: Int
    )

    @Serializable
    data class Players(
        val max: Int,
        val online: Int,
        val sample: List<Player>
    ) {

        @Serializable
        data class Player(
            val id: SerialUUID,
            val name: String
        )

    }

    @JvmInline
    @Serializable
    value class Description(
        val text: String
    )

}
