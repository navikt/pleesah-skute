val ktorVersion = "3.5.1"
val pleesahMainClass = "org.example.AppKt"


plugins {
    alias(libs.plugins.kotlin.jvm)
    alias(libs.plugins.kotlin.serialization)
    application
}

repositories {
    mavenCentral()
}

dependencies {
    implementation(libs.guava)

    implementation("io.ktor:ktor-server-core:$ktorVersion")
    implementation("io.ktor:ktor-server-netty:$ktorVersion")
    implementation("io.ktor:ktor-client-core:${ktorVersion}")
    implementation("io.ktor:ktor-client-cio:${ktorVersion}")
    implementation("io.ktor:ktor-server-content-negotiation:$ktorVersion")
    implementation("io.ktor:ktor-serialization-kotlinx-json:$ktorVersion")
    implementation("org.slf4j:slf4j-simple:2.0.17")
    testImplementation(kotlin("test"))
    testImplementation("io.ktor:ktor-server-test-host:$ktorVersion")
    testImplementation("io.ktor:ktor-client-mock:$ktorVersion")
}

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(24)
    }
}

application {
    mainClass.set(pleesahMainClass)
    applicationDefaultJvmArgs = listOf(
        "--enable-native-access=ALL-UNNAMED", // For å stilne logger fra Netty
        "--add-opens", "java.base/jdk.internal.misc=ALL-UNNAMED",
        "--add-opens", "java.base/sun.nio.ch=ALL-UNNAMED",
        "-XX:+UnlockDiagnosticVMOptions",
        "-Dio.netty.tryReflectionSetAccessible=true",
        "--sun-misc-unsafe-memory-access=allow" // Suppress sun.misc.Unsafe warnings
    )
}

tasks {
    register<JavaExec>("runMockHavnesjef") {
        group = "application"
        description = "Kjører en lokal test-dobbel for havnesjef.pleesah-system/teams"
        mainClass.set("org.example.MockHavnesjefKt")
        classpath = sourceSets["test"].runtimeClasspath
    }

    withType<Jar> {
        archiveBaseName.set("app")

        manifest {
            attributes["Main-Class"] = pleesahMainClass
            attributes["Class-Path"] =
                configurations.runtimeClasspath.get().joinToString(separator = " ") {
                    it.name
                }
        }
        doLast {
            configurations.runtimeClasspath.get().forEach {
                val file = File("${layout.buildDirectory.get()}/libs/${it.name}")
                if (!file.exists()) it.copyTo(file)
            }
        }
    }
}
