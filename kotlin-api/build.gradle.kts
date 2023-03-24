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
    "log4j" to "2.20.0",
    "slf4j" to "2.0.6"
)

dependencies {
    implementation("org.jsoup:jsoup:${versions["jsoup"]}")
    implementation("org.apache.pdfbox:pdfbox:${versions["pdfbox"]}")

    implementation("org.apache.logging.log4j:log4j-api:${versions["log4j"]}")
    implementation("org.apache.logging.log4j:log4j-core:${versions["log4j"]}")
    implementation("org.apache.logging.log4j:log4j-slf4j2-impl:${versions["log4j"]}")

    implementation("org.slf4j:slf4j-api:${versions["slf4j"]}")

    testImplementation(kotlin("test"))
}

tasks {
    test {
        useJUnitPlatform {
            if(System.getenv("CI") != null) {
                excludeTags("CI_EXCLUDE")
            }
        }
    }

    register<Test>("onlyV2Tests") {
        useJUnitPlatform {
            includeTags("v2")
            if(System.getenv("CI") != null) {
                excludeTags("CI_EXCLUDE")
            }
        }
        group = "verification"
    }
}

kotlin {
    jvmToolchain(17)
}