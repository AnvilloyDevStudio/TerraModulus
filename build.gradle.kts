import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    kotlin("jvm") version "2.1.20"
    kotlin("plugin.serialization") version "2.1.20"
    id("org.jetbrains.kotlinx.atomicfu") version "0.27.0"
    application
}

allprojects {
    apply(plugin = "org.jetbrains.kotlin.jvm")
    apply(plugin = "application")

    version = "0.1.0"

    repositories {
        mavenCentral()
    }
}

subprojects {
    sourceSets.main {
        kotlin.srcDir("kotlin")
        resources.srcDir("resources")
    }

    tasks.compileKotlin {
        compilerOptions {
            jvmTarget.set(JvmTarget.JVM_21)
        }
    }

    kotlin {
        jvmToolchain(21)
    }
}

project(":common") {
    dependencies {
        api("org.jetbrains:annotations:26.0.2")
        api("org.jetbrains.kotlinx:kotlinx-serialization-json:1.8.1")
        api("org.jetbrains.kotlinx:kotlinx-io-core:0.7.0")
        api("org.jetbrains.kotlinx:kotlinx-datetime:0.6.2")
        api("org.jetbrains.kotlinx:multik-core:0.2.3")
        api("org.jetbrains.kotlinx:multik-default:0.2.3")
        api(kotlin("reflect"))
        api("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.10.2")
    }
}

project(":client") {
    dependencies {
        api(project(":common"))
    }

    application {
        mainClass = "terramodulus.client.Main"
    }
}

project(":server") {
    dependencies {
        api(project(":common"))
    }

    application {
        mainClass = "terramodulus.server.Main"
    }
}
