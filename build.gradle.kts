plugins {
    kotlin("jvm") version "1.8.0"
}

group = "edu.tum.romance.whatsthis"
version = "1.0.0"

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