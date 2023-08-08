package dev.h4kt.minecraftquery.engines.tcp.responses

import dev.h4kt.minecraftquery.serializers.SerialUUID
import kotlinx.serialization.Serializable

@Serializable
internal data class TCPStatusResponse(
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
