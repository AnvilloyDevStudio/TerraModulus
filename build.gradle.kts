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
        api("com.github.oshi:oshi-core:6.8.0")
        api("net.java.dev.jna:jna:5.17.0")
        api("net.java.dev.jna:jna-platform:5.17.0")
    }
}

project(":client") {
    dependencies {
        api(project(":common"))
    }

    application {
        mainClass = "terramodulus.core.MainKt"
    }
}

project(":server") {
    dependencies {
        api(project(":common"))
    }

    application {
        mainClass = "terramodulus.core.MainKt"
    }
}

enum class Target {
    CLIENT, SERVER;
}

/** Build Ferricia Engine with Cargo */
fun Exec.configureCargoBuild(target: Target) {
    workingDir = rootProject.file("ferricia")
    commandLine("cargo", "build")
    if (project.hasProperty("release")) args("--release") // use `-Prelease=true`
    args("-F")
    when (target) {
        Target.CLIENT -> args("client")
        Target.SERVER -> args("server")
    }
}

tasks.register<Exec>("run_client") {
    group = "application"
    description = "Run client"
    finalizedBy(":client:run")
    configureCargoBuild(Target.CLIENT)
}

tasks.register<Exec>("run_server") {
    group = "application"
    description = "Run server"
    finalizedBy(":server:run")
    configureCargoBuild(Target.SERVER)
}

configure(listOf(project(":server"), project(":client"))) {
    tasks.named<JavaExec>("run") {
        jvmArgs("-Djava.library.path=${rootProject.file("ferricia/target/${
            if (project.hasProperty("release")) "release" else "debug"
        }").path}")
    }
}
