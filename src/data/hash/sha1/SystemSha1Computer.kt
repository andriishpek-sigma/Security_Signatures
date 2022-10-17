package data.hash.sha1

import java.security.MessageDigest

class SystemSha1Computer : Sha1Computer {

    private val messageDigest: MessageDigest by lazy(LazyThreadSafetyMode.NONE) {
        MessageDigest.getInstance("SHA-1")
    }

    override fun hash(message: ByteArray): ByteArray {
        return messageDigest.digest(message)
    }
}