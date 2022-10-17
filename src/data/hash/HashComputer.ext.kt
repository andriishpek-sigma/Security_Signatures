package data.hash

import java.nio.charset.Charset

fun HashComputer.hash(message: String, charset: Charset = Charsets.UTF_8): ByteArray {
    return hash(message.toByteArray(charset))
}