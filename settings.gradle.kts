pluginManagement {
    repositories {
        google()  // Google's Maven repository
        gradlePluginPortal()
        mavenCentral()
    }
}

rootProject.name = "MobileClient"
include(":androidApps")
include(":common")
//include(":lib:KMQTT-Client")
