import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.7.10"
    kotlin("plugin.serialization") version "1.7.10"

    id("org.openjfx.javafxplugin") version "0.0.13"
}

group = "org.example"
version = "1.0-SNAPSHOT"

sourceSets {
    main {
        java {
            srcDirs("D")
        }
    }
}

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(kotlin("test"))

    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.4.0")

    runtimeOnly("org.jetbrains.kotlin:kotlin-runtime:1.2.71")
}

tasks.test {
    useJUnitPlatform()
}

tasks.jar {
    duplicatesStrategy = DuplicatesStrategy.EXCLUDE
    manifest {
        attributes["Main-Class"] = "D.Other.XGuiKt"
    }

    from(configurations.runtimeClasspath.get().map { if (it.isDirectory()) it else zipTree(it) })
    destinationDirectory.set(File("D/Other"))
}

tasks.withType<KotlinCompile> {
    kotlinOptions.jvmTarget = "11"
}

javafx {
    version = "19"
    modules = mutableListOf("javafx.controls", "javafx.fxml", "javafx.base")
}