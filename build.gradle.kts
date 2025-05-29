import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    kotlin("jvm") version "2.1.20"
    kotlin("plugin.serialization") version "2.1.20"
    id("org.jetbrains.kotlinx.atomicfu") version "0.27.0"
    application
}

configure(listOf(project(":kernel:server"), project(":kernel:client"))) {
    apply(plugin = "application")
}

allprojects {
    apply(plugin = "org.jetbrains.kotlin.jvm")

    version = "0.1.0"

    repositories {
        mavenCentral()
    }
}

configure(listOf(project(":kernel"), project(":internal"))) {
    configure(listOf(project("common"), project("client"), project("server"))) {
        sourceSets.main {
            kotlin.srcDir("kotlin")
            resources.srcDir("resources")
        }

        tasks.compileKotlin {
            compilerOptions {
                jvmTarget.set(JvmTarget.JVM_17)
            }
        }

        kotlin {
            jvmToolchain(17)
        }
    }

    configure(listOf(project("client"), project("server"))) {
        dependencies {
            implementation(project("${parent!!.path}:common"))
        }
    }
}

project(":kernel") {
    arrayOf("common", "client", "server").forEach {
        project(it) {
            dependencies {
                implementation(project(":internal:$it"))
            }
        }
    }
}

project(":kernel:common") {
    dependencies {
        api("org.jetbrains:annotations:26.0.2")
        api("org.jetbrains.kotlinx:kotlinx-serialization-json:1.8.1")
        api("org.jetbrains.kotlinx:kotlinx-io-core:0.7.0")
        api("org.jetbrains.kotlinx:kotlinx-datetime:0.6.2")
        api("org.jetbrains.kotlinx:multik-core:0.2.3")
        api("org.jetbrains.kotlinx:multik-default:0.2.3")
        api(kotlin("reflect"))
        api("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.10.2")
        implementation("com.github.oshi:oshi-core:6.8.0")
        api("com.google.errorprone:error_prone_annotations:2.38.0")
        implementation("org.apache.logging.log4j:log4j-core:2.24.3")
        implementation("org.apache.logging.log4j:log4j-api:2.24.3")
        implementation("org.apache.logging.log4j:log4j-slf4j2-impl:2.24.3")
        implementation(platform("org.apache.logging.log4j:log4j-bom:2.24.3"))
        annotationProcessor("org.apache.logging.log4j:log4j-core:2.24.3")
        runtimeOnly("com.lmax:disruptor:4.0.0")
        api("io.github.oshai:kotlin-logging-jvm:7.0.3")
        implementation("net.sf.jopt-simple:jopt-simple:5.0.4")
    }
}

project(":kernel:client").dependencies {
    implementation("net.sf.jopt-simple:jopt-simple:5.0.4")
}

project(":kernel:server").dependencies {
    implementation("net.sf.jopt-simple:jopt-simple:5.0.4")
}

project(":internal:common").dependencies {
    implementation("net.java.dev.jna:jna:5.17.0")
    implementation("net.java.dev.jna:jna-platform:5.17.0")
}

configure(listOf(project(":kernel:server"), project(":kernel:client"))) {
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

tasks.register<Exec>("runClient") {
    group = "application"
    description = "Run client"
    finalizedBy(":kernel:client:run")
    configureCargoBuild(Target.CLIENT)
}

tasks.register<Exec>("runServer") {
    group = "application"
    description = "Run server"
    finalizedBy(":kernel:server:run")
    configureCargoBuild(Target.SERVER)
}

configure(listOf(project(":kernel:server"), project(":kernel:client"))) {
    tasks.named<JavaExec>("run") {
        jvmArgs("-Djava.library.path=${rootProject.file("ferricia/target/${
            if (project.hasProperty("release")) "release" else "debug"
        }").path}")
    }
}
