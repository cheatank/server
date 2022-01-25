import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar

plugins {
    kotlin("jvm") version "1.6.0"
    id("org.jmailen.kotlinter") version "3.7.0"
    id("com.github.ben-manes.versions") version "0.41.0"
    id("com.github.johnrengelman.shadow") version "7.1.2"
    application
}

group = "com.github.cheatank"
version = "1.0.0-SNAPSHOT"

repositories {
    mavenCentral()
    mavenLocal()
}

dependencies {
    implementation(kotlin("stdlib"))
    implementation("com.github.cheatank:common:1.0.0-SNAPSHOT")
    implementation("io.ktor:ktor-server-core:1.6.7")
    implementation("io.ktor:ktor-server-cio:1.6.7")
    implementation("io.ktor:ktor-websockets:1.6.7")
    implementation("org.slf4j:slf4j-simple:1.7.33")
    testImplementation(kotlin("test"))
    testImplementation("io.ktor:ktor-server-test-host:1.6.7")
}

application {
    mainClass.set("com.github.cheatank.server.MainKt")
}

tasks.withType<ShadowJar> {
    archiveClassifier.set("")
}
