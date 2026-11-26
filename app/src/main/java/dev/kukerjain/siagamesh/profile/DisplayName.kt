package dev.kukerjain.siagamesh.profile

/** Aturan nama tampilan. Murni (tanpa Android) supaya bisa dites di JVM. */
object DisplayName {
    const val MAX_LENGTH = 32

    sealed interface Result {
        data class Valid(val name: String) : Result
        data object Empty : Result
        data object TooLong : Result
    }

    private val whitespace = Regex("\\s+")

    /** Rapikan spasi lalu validasi. Panjang dihitung per code point agar emoji tidak dihitung dua. */
    fun validate(raw: String): Result {
        val name = raw.trim().replace(whitespace, " ")
        return when {
            name.isEmpty() -> Result.Empty
            name.codePointCount(0, name.length) > MAX_LENGTH -> Result.TooLong
            else -> Result.Valid(name)
        }
    }
}
