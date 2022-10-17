package data.hash

interface HashComputer {

    fun hash(message: ByteArray): ByteArray

}