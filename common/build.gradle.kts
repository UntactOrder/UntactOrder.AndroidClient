plugins {
    kotlin("multiplatform")
    id("com.android.library")
}

kotlin {
    android()
    
    listOf(
        iosX64(),
        iosArm64(),
        iosSimulatorArm64()
    ).forEach {
        it.binaries.framework {
            baseName = "common"
        }
    }

    sourceSets {
        val commonMain by getting
        val commonTest by getting {
            dependencies {
                implementation(kotlin("test"))
            }
        }
        val androidMain by getting
        val androidTest by getting
        val iosX64Main by getting
        val iosArm64Main by getting
        val iosSimulatorArm64Main by getting
        val iosMain by creating {
            dependsOn(commonMain)
            iosX64Main.dependsOn(this)
            iosArm64Main.dependsOn(this)
            iosSimulatorArm64Main.dependsOn(this)
        }
        val iosX64Test by getting
        val iosArm64Test by getting
        val iosSimulatorArm64Test by getting
        val iosTest by creating {
            dependsOn(commonTest)
            iosX64Test.dependsOn(this)
            iosArm64Test.dependsOn(this)
            iosSimulatorArm64Test.dependsOn(this)
        }
    }
}

android {
    compileSdk = 33
    buildToolsVersion = "33.0.0"
    sourceSets["main"].manifest.srcFile("src/androidMain/AndroidManifest.xml")
    defaultConfig {
        minSdk = 24
        targetSdk = 33
    }
}

dependencies {
    // Ktor Client dependencies
    implementation("io.ktor:ktor-client-core:${rootProject.extra["ktor_version"]}")
    implementation("io.ktor:ktor-client-cio:${rootProject.extra["ktor_version"]}")
    implementation("io.ktor:ktor-server-core:${rootProject.extra["ktor_version"]}")
    api("org.eclipse.paho:org.eclipse.paho.mqttv5.client:${rootProject.extra["paho_mqtt_client_version"]}")
    testImplementation("io.ktor:ktor-server-test-host:${rootProject.extra["ktor_version"]}")

    testImplementation("org.junit.jupiter:junit-jupiter")
    testImplementation("junit:junit:4.13.2")
}
