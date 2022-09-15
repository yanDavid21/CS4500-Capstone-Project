import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.7.10"
    kotlin("plugin.serialization") version "1.7.10"
}

group = "org.example"
version = "1.0-SNAPSHOT"

sourceSets {
    main {
        java {
            srcDirs("C")
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
        attributes["Main-Class"] = "C.Other.XJsonKt"
    }

    from(configurations.compileClasspath.get().map { if (it.isDirectory()) it else zipTree(it) })
    destinationDirectory.set(File("C/Other"))


}

tasks.withType<KotlinCompile> {
    kotlinOptions.jvmTarget = "1.8"
}