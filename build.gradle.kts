plugins {
    kotlin("jvm") version "1.6.0"
    id("org.jmailen.kotlinter") version "3.7.0"
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
