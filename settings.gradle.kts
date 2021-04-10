pluginManagement {
    plugins {
        val kotlinVersion: String by settings
        val detektVersion: String by settings
        val ideaExtPluginVersion: String by settings
        kotlin("multiplatform") version kotlinVersion
        id("de.otto.find.project-version") version "1.1.0"
        id("org.jetbrains.dokka") version "1.4.30"
        id("io.gitlab.arturbosch.detekt") version detektVersion
        id("org.jetbrains.gradle.plugin.idea-ext") version ideaExtPluginVersion
    }
}

rootProject.name = "disjunkt"
