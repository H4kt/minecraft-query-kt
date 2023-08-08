package dev.h4kt.minecraftquery.extensions

import io.ktor.utils.io.*

suspend fun ByteReadChannel.readNullTerminatedString(): String {

    if (availableForRead == 0) {
        return ""
    }

    val data = mutableListOf<Byte>()
    while (true) {

        val byte = readByte()
        if (byte == 0.toByte()) {
            break
        }

        data += byte

    }

    return data.toByteArray().toString(Charsets.UTF_8)
}
