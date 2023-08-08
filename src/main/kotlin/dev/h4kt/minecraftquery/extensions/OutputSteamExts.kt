package dev.h4kt.minecraftquery.extensions

import java.io.OutputStream

fun OutputStream.writeVarInt(value: Int) {

    var intValue = value

    do {

        var tempValue = intValue and 0x7F
        intValue = intValue ushr 7

        if (intValue != 0) {
            tempValue = tempValue or 0x80
        }

        write(tempValue)

    } while (intValue != 0)

}

fun OutputStream.writeUShortBigEndian(value: UShort) {
    val highByte = (value.toInt() ushr 8) and 0xFF
    val lowByte = value.toInt() and 0xFF
    write(highByte)
    write(lowByte)
}

fun OutputStream.writeUShortLittleEndian(value: UShort) {
    val lowByte = value.toInt() and 0xFF
    val highByte = (value.toInt() ushr 8) and 0xFF
    write(lowByte)
    write(highByte)
}

fun OutputStream.writeIntBigEndian(value: Int) {
    write((value shr 24) and 0xFF)
    write((value shr 16) and 0xFF)
    write((value shr 8) and 0xFF)
    write(value and 0xFF)
}
