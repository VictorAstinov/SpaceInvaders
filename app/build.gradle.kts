plugins {
    application
    kotlin("jvm") version "1.8.20"
    id("org.openjfx.javafxplugin") version "0.0.14"
}

group = "org.a3"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(kotlin("test"))
}

tasks.test {
    useJUnitPlatform()
}

kotlin {
    jvmToolchain(17)
}

application {
    mainClass.set("SpaceInvaders")
}

javafx {
    version = "18.0.2"
    modules("javafx.controls", "javafx.graphics", "javafx.media")
}