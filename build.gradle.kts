tasks {
    register("test") {
        dependsOn(project(":kotlin-api").tasks.named("test"))
    }
}