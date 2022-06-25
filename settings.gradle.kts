pluginManagement {
    repositories {
        google()  // Google's Maven repository
        gradlePluginPortal()
        mavenCentral()
		
		maven {
            // Adding Kakao SDK Repo
            url 'https://devrepo.kakao.com/nexus/content/groups/public/'
        }
    }
}

rootProject.name = "MobileClient"
include(":androidApps")
include(":common")