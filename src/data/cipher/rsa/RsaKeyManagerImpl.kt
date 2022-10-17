package data.cipher.rsa

import java.security.KeyFactory
import java.security.PrivateKey
import java.security.PublicKey
import java.security.spec.PKCS8EncodedKeySpec
import java.security.spec.X509EncodedKeySpec

class RsaKeyManagerImpl : RsaKeyManager {

    private val keyFactory: KeyFactory by lazy {
        KeyFactory.getInstance("RSA")
    }

    override fun decodePublicKey(key: ByteArray): PublicKey {
        val keySpec = X509EncodedKeySpec(key)
        return keyFactory.generatePublic(keySpec)
    }

    override fun decodePrivateKey(key: ByteArray): PrivateKey {
        val keySpec = PKCS8EncodedKeySpec(key)
        return keyFactory.generatePrivate(keySpec)
    }

    override fun encodePublicKey(key: PublicKey): ByteArray {
        return key.encoded
    }

    override fun encodePrivateKey(key: PrivateKey): ByteArray {
        return key.encoded
    }

}