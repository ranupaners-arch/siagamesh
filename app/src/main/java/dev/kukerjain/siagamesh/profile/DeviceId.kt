package dev.kukerjain.siagamesh.profile

import java.security.SecureRandom
import java.util.Random

/**
 * ID perangkat acak 64-bit, dibuat sekali saat first launch lalu disimpan.
 * Bukan identitas kriptografis — key pair ECDH menyusul di fase grup.
 */
object DeviceId {
    const val BYTES = 8
    private val pattern = Regex("^[0-9a-f]{${BYTES * 2}}$")

    fun generate(random: Random = SecureRandom()): String {
        val bytes = ByteArray(BYTES).also(random::nextBytes)
        return bytes.joinToString("") { "%02x".format(it.toInt() and 0xff) }
    }

    fun isValid(id: String?): Boolean = id != null && pattern.matches(id)

    /** "a1b2c3d4e5f60718" → "A1B2 C3D4 E5F6 0718", lebih mudah dibaca/didikte. */
    fun pretty(id: String): String = id.uppercase().chunked(4).joinToString(" ")
}
