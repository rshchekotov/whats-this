plugins {
    kotlin("jvm") version "1.8.0"
}

group = "edu.tum.romance.whatsthis"
version = "1.0.0"

repositories {
    mavenCentral()
}

@Suppress("SpellCheckingInspection")
dependencies {
    implementation("org.jsoup:jsoup:1.15.3")
    implementation("org.apache.pdfbox:pdfbox:3.0.0-alpha2")
    implementation("com.formdev:flatlaf:3.0")

    testImplementation(kotlin("test"))
}

tasks.test {
    useJUnitPlatform()
}

kotlin {
    jvmToolchain(17)
}