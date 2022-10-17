package data.signature

interface SignatureManager {

    fun createSignature(
        data: ByteArray,
        publicKey: ByteArray
    ): ByteArray

    fun verifySignature(
        data: ByteArray,
        signature: ByteArray,
        privateKey: ByteArray
    ): Boolean

}