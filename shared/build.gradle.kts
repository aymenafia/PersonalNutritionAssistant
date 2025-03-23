import java.io.File

plugins {
    id("org.jetbrains.kotlin.multiplatform") version "2.0.0"
    id("com.android.library") version "8.9.0"
    id("org.jetbrains.kotlin.plugin.serialization") version "2.0.0"
    id("com.squareup.sqldelight") version "1.5.5"
}

kotlin {
    android {
        compilations.all {
            kotlinOptions {
                jvmTarget = "1.8"
            }
        }
    }

    listOf(iosArm64(), iosSimulatorArm64()).forEach { iosTarget ->
        iosTarget.binaries.framework {
            baseName = "shared"
            isStatic = true
            linkerOpts("-lsqlite3")
        }
    }

    sourceSets {
        val ktorVersion = "2.3.4"
        val serializationVersion = "1.6.3"
        val datetimeVersion = "0.4.0"
        val sqlDelightVersion = "1.5.5"

        val commonMain by getting {
            dependencies {
                implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:$serializationVersion")
                implementation("org.jetbrains.kotlinx:kotlinx-datetime:$datetimeVersion")
                implementation("me.tatarka.inject:kotlin-inject-runtime:0.6.0")

                implementation("io.ktor:ktor-client-core:$ktorVersion")
                implementation("io.ktor:ktor-client-content-negotiation:$ktorVersion")
                implementation("io.ktor:ktor-client-mock:$ktorVersion")
                implementation("io.ktor:ktor-serialization-kotlinx-json:$ktorVersion")

                implementation("com.squareup.sqldelight:runtime:$sqlDelightVersion")
            }
        }
        val commonTest by getting {
            dependencies {
                implementation(kotlin("test"))
            }
        }
        val androidMain by getting {
            dependencies {
                implementation("com.squareup.sqldelight:android-driver:$sqlDelightVersion")
            }
        }
        val iosMain by creating {
            dependsOn(commonMain)
            dependencies {
                implementation("com.squareup.sqldelight:native-driver:$sqlDelightVersion")
            }
        }
        val iosTest by creating {
            dependsOn(commonTest)
        }
        listOf(iosArm64(), iosSimulatorArm64()).forEach {
            it.compilations["main"].defaultSourceSet.dependsOn(iosMain)
            it.compilations["test"].defaultSourceSet.dependsOn(iosTest)
        }
    }
}

sqldelight {
    database("MealsDatabase") {
        packageName = "com.example.personalnutritionassistant.database"
    }
}

android {
    namespace = "com.example.personalnutritionassistant"
    compileSdk = 35
    defaultConfig {
        minSdk = 24
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
}

tasks.register("createXCFramework", Exec::class) {
    dependsOn("linkDebugFrameworkIosArm64", "linkDebugFrameworkIosSimulatorArm64")
    val outputDir = file("$buildDir/XCFramework")
    doFirst {
        outputDir.mkdirs()
    }
    commandLine(
        "xcodebuild", "-create-xcframework",
        "-framework", "$buildDir/bin/iosArm64/debugFramework/shared.framework",
        "-framework", "$buildDir/bin/iosSimulatorArm64/debugFramework/shared.framework",
        "-output", "$outputDir/shared.xcframework"
    )
}