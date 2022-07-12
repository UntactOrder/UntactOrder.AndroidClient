pluginManagement {
    repositories {
        google()  // Google's Maven repository
        gradlePluginPortal()
        mavenLocal()
        mavenCentral()
    }
}

rootProject.name = "MobileClient"
include(":androidApps")
include(":common")
