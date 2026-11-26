plugins {
    alias(libs.plugins.android.application) apply false
    // Hanya menaruh KGP versi ini di classpath; plugin-nya tidak di-apply (AGP 9 built-in Kotlin).
    alias(libs.plugins.kotlin.android) apply false
}
