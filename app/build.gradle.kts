plugins {
    alias(libs.plugins.android.application)
}

android {
    namespace = "dev.kukerjain.siagamesh"
    compileSdk = 36

    defaultConfig {
        applicationId = "dev.kukerjain.siagamesh"
        minSdk = 24
        targetSdk = 36
        versionCode = 1
        versionName = "0.1.0-dev"
    }

    buildTypes {
        release {
            isMinifyEnabled = true
            isShrinkResources = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro",
            )
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    buildFeatures {
        buildConfig = true
    }

    // Jaga APK tetap kecil: tidak perlu metadata dependensi di APK.
    dependenciesInfo {
        includeInApk = false
        includeInBundle = false
    }
}

// Sengaja tanpa AndroidX/Material: framework Activity + Theme.Material bawaan
// cukup untuk HP murah dan menjaga APK di bawah 3 MB.
dependencies {
    testImplementation(libs.junit)
}
