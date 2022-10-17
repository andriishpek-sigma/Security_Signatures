package data.cipher.rsa

import java.security.PrivateKey
import java.security.PublicKey

interface RsaKeyDecoder {

    fun decodePublicKey(key: ByteArray): PublicKey

    fun decodePrivateKey(key: ByteArray): PrivateKey

}