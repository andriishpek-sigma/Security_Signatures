package data.cipher.rsa

import java.security.PrivateKey
import java.security.PublicKey

interface RsaKeyEncoder {

    fun encodePublicKey(key: PublicKey): ByteArray

    fun encodePrivateKey(key: PrivateKey): ByteArray

}