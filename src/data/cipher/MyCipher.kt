package data.cipher

interface MyCipher {

    fun encrypt(data: ByteArray, keyword: ByteArray): ByteArray

    fun decrypt(data: ByteArray, keyword: ByteArray): ByteArray

}