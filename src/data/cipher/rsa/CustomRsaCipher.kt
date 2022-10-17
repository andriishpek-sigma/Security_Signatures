package data.cipher.rsa

import java.util.*
import javax.crypto.Cipher

class CustomRsaCipher(
    private val keyDecoder: RsaKeyDecoder,
) : RsaCipher {

    private val cipher: Cipher by lazy {
        Cipher.getInstance("RSA/ECB/PKCS1Padding")
    }

    override fun encrypt(data: ByteArray, keyword: ByteArray): ByteArray {
        val publicKey = keyDecoder.decodePublicKey(keyword)
        cipher.init(Cipher.ENCRYPT_MODE, publicKey)
        val encrypted = cipher.doFinal(data)
        return Base64.getEncoder().encode(encrypted)
    }

    override fun decrypt(data: ByteArray, keyword: ByteArray): ByteArray {
        val decoded = Base64.getDecoder().decode(data)
        val privateKey = keyDecoder.decodePrivateKey(keyword)
        cipher.init(Cipher.DECRYPT_MODE, privateKey)
        return cipher.doFinal(decoded)
    }
}