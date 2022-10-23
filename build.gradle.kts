import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.7.10"
    kotlin("plugin.serialization") version "1.7.10"

    id("org.openjfx.javafxplugin") version "0.0.13"
}

group = "org.example"
version = "1.0-SNAPSHOT"

sourceSets {
    test {
        java {
            srcDirs("test")
        }
    }

    main {
        java {
            srcDirs("Maze")
        }
    }
}

repositories {
    mavenCentral()
}

dependencies {
    testImplementation("org.junit.jupiter:junit-jupiter:5.8.1")
    testImplementation(kotlin("test"))

    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.4.0")
    implementation("com.google.code.gson:gson:2.9.1")

    runtimeOnly("org.jetbrains.kotlin:kotlin-runtime:1.2.71")
    runtimeOnly("org.jetbrains.kotlin:kotlin-reflect:1.7.0")

    runtimeOnly("org.openjfx:javafx-base:18.0.2:linux")
    runtimeOnly("org.openjfx:javafx-graphics:18.0.2:linux")
    runtimeOnly("org.openjfx:javafx-controls:18.0.2:linux")
    runtimeOnly("org.openjfx:javafx-fxml:18.0.2:linux")
    runtimeOnly("org.openjfx:javafx-media:18.0.2:linux")
    runtimeOnly("org.openjfx:javafx-swing:18.0.2:linux")

}

tasks.test {
    useJUnit()
}

tasks.jar {
    duplicatesStrategy = DuplicatesStrategy.EXCLUDE
    manifest {
        attributes["Main-Class"] = "testing/StrategyIntegrationTestsKt"
    }

    from(configurations.compileClasspath.get().map { if (it.isDirectory()) it else zipTree(it) })

    destinationDirectory.set(File("5/Other"))
}

tasks.withType<KotlinCompile> {
    kotlinOptions.jvmTarget = "11"
}

javafx {
    version = "19"
    modules = mutableListOf("javafx.controls", "javafx.fxml" , "javafx.base", "javafx.media", "javafx.graphics")
}