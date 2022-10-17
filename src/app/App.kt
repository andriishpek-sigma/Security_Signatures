@file:Suppress("SameParameterValue")

package app

import data.cipher.rsa.CustomRsaCipher
import data.cipher.rsa.RsaKeyGeneratorImpl
import data.cipher.rsa.RsaKeyManager
import data.cipher.rsa.RsaKeyManagerImpl
import data.hash.sha1.SystemSha1Computer
import data.signature.SignatureManager
import data.signature.SignatureManagerImpl
import data.utils.hexToByteArray
import data.utils.toHexString
import java.io.File

class App {

    companion object {
        private const val INPUT_FILE = "input.txt"
        private const val SIG_FILE = "sig.txt"
    }

    private val keyManger : RsaKeyManager = RsaKeyManagerImpl()

    private val rsaKeyGenerator = RsaKeyGeneratorImpl(keyManger, 2048)

    private val signatureManager: SignatureManager = SignatureManagerImpl(
        hashComputer = SystemSha1Computer(),
        cipher = CustomRsaCipher(keyManger),
    )

    fun execute() {
        executeForExternalFile()
        print("\n\n\n")
        executeForStringInput()
    }

    private fun executeForExternalFile() {
        println("-> File input $INPUT_FILE")
        val fileBytes = readFile(INPUT_FILE)
        println("File size: ${fileBytes.size}\n")
        execute(fileBytes, INPUT_FILE)
    }

    private fun executeForStringInput() {
        println("-> String input")
        print("Enter message: ")
        val message = readLine() ?: return
        println()
        execute(message.encodeToByteArray(), "message")
    }

    private fun execute(data: ByteArray, prefix: String) {
        val keys = rsaKeyGenerator.generateKeys()
        val signatureFileName = "${prefix}_$SIG_FILE"

        val createdSignature = signatureManager.createSignature(
            data = data,
            publicKey = keys.public
        )
        val createdSignatureHex = createdSignature.toHexString().uppercase()
        println("Created sig: $createdSignatureHex")

        writeFileText(signatureFileName, createdSignatureHex)
        println("Created sig saved into $signatureFileName\n")

        val loadedSignatureHex = readFileText(signatureFileName)
        val loadedSignature = loadedSignatureHex.hexToByteArray()
        println("Sig loaded from $signatureFileName")
        println("Loaded sig: $loadedSignatureHex\n")

        val isSignatureValid = signatureManager.verifySignature(
            data = data,
            signature = loadedSignature,
            privateKey = keys.private
        )
        println("Sig valid: ? $isSignatureValid")
    }

    private fun readFile(name: String): ByteArray {
        return fileNameInDir(name).readBytes()
    }

    private fun readFileText(name: String): String {
        return fileNameInDir(name).readText()
    }

    private fun writeFile(name: String, data: ByteArray) {
        return fileNameInDir(name).writeBytes(data)
    }

    private fun writeFileText(name: String, data: String) {
        return fileNameInDir(name).writeText(data)
    }

    private fun fileNameInDir(name: String): File {
        return File("data", name).also {
            it.parentFile.mkdirs()
            it.createNewFile()
        }
    }
}