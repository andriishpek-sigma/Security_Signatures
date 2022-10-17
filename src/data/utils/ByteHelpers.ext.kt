@file:Suppress("unused")

package data.utils

fun ByteArray.toHexString(): String {
    return joinToString(separator = "") { it.toHexString() }
}

fun Byte.toHexString(): String {
    return "%02x".format(this)
}

infix fun ByteArray.xor(other: ByteArray) : ByteArray {
    if (size != other.size) {
        throw RuntimeException("Block length must be equal")
    }

    return ByteArray(size) { i ->
        val one = this[i].toInt()
        val two = other[i].toInt()
        val xor = one xor two
        (0xff and xor).toByte()
    }
}

fun String.hexToByteArray(): ByteArray {
    require(length % 2 == 0) {
        "Hex string must have an even length!"
    }

    return chunked(2)
        .map { it.toInt(16).toByte() }
        .toByteArray()
}
