@file:Suppress("SpellCheckingInspection")

plugins {
    kotlin("jvm") version "1.8.0"
}

group = "edu.tum.romance.whatsthis"
version = "1.0.0"

repositories {
    mavenCentral()
}

val versions = mapOf(
    "jsoup" to "1.15.3",
    "pdfbox" to "3.0.0-alpha2",
    "jackson" to "2.14.2",
    "flatlaf" to "3.0"
)

dependencies {
    implementation("org.jsoup:jsoup:${versions["jsoup"]}")
    implementation("org.apache.pdfbox:pdfbox:${versions["pdfbox"]}")

    // Nothing can be done about this at the moment.
    // We'll wait 'til Jackson upgrades its SnakeYAML to (the already released) v2.0
    @Suppress("VulnerableLibrariesLocal")
    implementation("com.fasterxml.jackson.core:jackson-databind:${versions["jackson"]}")
    implementation("com.fasterxml.jackson.dataformat:jackson-dataformat-yaml:${versions["jackson"]}")

    implementation("com.formdev:flatlaf:${versions["flatlaf"]}")

    testImplementation(kotlin("test"))
}

tasks.test {
    useJUnitPlatform()
}

kotlin {
    jvmToolchain(17)
}