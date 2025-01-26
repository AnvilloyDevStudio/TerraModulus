import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    kotlin("jvm") version "2.1.0"
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
        implementation("org.jetbrains:annotations:26.0.2")
    }
}

project(":client") {
    dependencies {
        implementation(project(":common"))
    }

    application {
        mainClass = "terramodulus.client.Main"
    }
}

project(":server") {
    dependencies {
        implementation(project(":common"))
    }

    application {
        mainClass = "terramodulus.server.Main"
    }
}
