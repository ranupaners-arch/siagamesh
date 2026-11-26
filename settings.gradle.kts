// Mirror resmi Google untuk Maven Central (isi artefak sama). Dipasang sebelum
// mavenCentral() karena repo.maven.apache.org bisa memberi 403 di sebagian jaringan.
val centralMirror = "https://maven-central.storage-download.googleapis.com/maven2/"

pluginManagement {
    repositories {
        google()
        maven("https://maven-central.storage-download.googleapis.com/maven2/") { name = "MavenCentralGoogleMirror" }
        mavenCentral()
        gradlePluginPortal()
    }
}

dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        maven(centralMirror) { name = "MavenCentralGoogleMirror" }
        mavenCentral()
    }
}

rootProject.name = "SiagaMesh"
include(":app")
