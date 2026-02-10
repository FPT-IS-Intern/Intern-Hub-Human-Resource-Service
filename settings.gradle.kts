rootProject.name = "Intern-Hub-Human-Resource-Service"

pluginManagement {
    repositories {
        gradlePluginPortal()
        mavenCentral()
        maven { url = uri("https://jitpack.io") }
    }
}

include(
    "common",
    "api",
    "core",
    "infra"
)