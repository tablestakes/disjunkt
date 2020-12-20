pluginManagement {
    plugins {
        val kotlinVersion: String by settings
        val detektVersion: String by settings
        val ideaExtPluginVersion: String by settings
        kotlin("multiplatform") version kotlinVersion
        id("io.gitlab.arturbosch.detekt") version detektVersion
        id("org.jetbrains.gradle.plugin.idea-ext") version ideaExtPluginVersion
    }
}
rootProject.name = "disjunct"
