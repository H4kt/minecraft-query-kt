package dev.h4kt.minecraftquery.serializers

import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import java.util.*

typealias SerialUUID = @Serializable(with = UUIDSerializer::class) UUID

object UUIDSerializer : KSerializer<UUID> {

    override val descriptor: SerialDescriptor = PrimitiveSerialDescriptor(
        serialName = "UUID",
        kind = PrimitiveKind.STRING
    )

    override fun deserialize(decoder: Decoder): UUID {
        return UUID.fromString(decoder.decodeString())
    }

    override fun serialize(
        encoder: Encoder,
        value: UUID
    ) {
        encoder.encodeString(value.toString())
    }

}
