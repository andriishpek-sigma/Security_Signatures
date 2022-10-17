package data.signature

import data.cipher.MyCipher
import data.hash.HashComputer

class SignatureManagerImpl(
    private val hashComputer: HashComputer,
    private val cipher: MyCipher,
) : SignatureManager {

    override fun createSignature(
        data: ByteArray,
        publicKey: ByteArray
    ): ByteArray {
        val messageHash = hashComputer.hash(data)
        return cipher.encrypt(messageHash, publicKey)
    }

    override fun verifySignature(
        data: ByteArray,
        signature: ByteArray,
        privateKey: ByteArray
    ): Boolean {
        val decryptedHash = cipher.decrypt(signature, privateKey)
        val calculatedHash = hashComputer.hash(data)

        return decryptedHash.contentEquals(calculatedHash)
    }
}