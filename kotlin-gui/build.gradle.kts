@file:Suppress("SpellCheckingInspection")

plugins {
    application
    kotlin("jvm") version "1.8.20-RC"
}

group = "edu.tum.romance.whatsthis.kui"
version = "1.0.0"

repositories {
    mavenCentral()
}

val versions = mapOf(
    "flatlaf" to "3.0",
    "log4j" to "2.20.0",
    "slf4j" to "2.0.6",
    "kotlin" to "1.8.20-RC",
    "reflections" to "0.10.2",
    "jackson" to "2.14.2"
)

dependencies {
    implementation("com.formdev:flatlaf:${versions["flatlaf"]}")

    implementation("org.apache.logging.log4j:log4j-api:${versions["log4j"]}")
    implementation("org.apache.logging.log4j:log4j-core:${versions["log4j"]}")
    implementation("org.apache.logging.log4j:log4j-slf4j2-impl:${versions["log4j"]}")

    implementation("org.slf4j:slf4j-api:${versions["slf4j"]}")

    implementation("org.reflections:reflections:${versions["reflections"]}")
    implementation(kotlin("reflect", versions["kotlin"]))

    // Nothing can be done about this at the moment.
    // We'll wait 'til Jackson upgrades its SnakeYAML to (the already released) v2.0
    @Suppress("VulnerableLibrariesLocal")
    implementation("com.fasterxml.jackson.core:jackson-databind:${versions["jackson"]}")
    implementation("com.fasterxml.jackson.dataformat:jackson-dataformat-yaml:${versions["jackson"]}")

    implementation(project(":kotlin-api"))

    testImplementation(kotlin("test"))
}

tasks {
    test {
        useJUnitPlatform()
    }

    register<Jar>("fatJar") {
        dependsOn("compileJava", "compileKotlin", "processResources")
        archiveClassifier.set("standalone")
        duplicatesStrategy = DuplicatesStrategy.EXCLUDE
        manifest {
            attributes["Main-Class"] = application.mainClass.get()
        }
        from(configurations.runtimeClasspath.get().map{
            if(it.isDirectory) it else zipTree(it)
        } + sourceSets.main.get().output)
    }

    build {
        dependsOn("fatJar")
    }
}

application {
    mainClass.set("edu.tum.romance.whatsthis.kui.Main")
}

kotlin {
    jvmToolchain(17)
}