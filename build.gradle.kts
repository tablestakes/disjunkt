import org.gradle.api.tasks.testing.logging.TestExceptionFormat
import org.gradle.api.tasks.testing.logging.TestLogEvent
import org.gradle.plugins.ide.idea.model.IdeaModule
import org.jetbrains.gradle.ext.ModuleSettings
import org.jetbrains.gradle.ext.PackagePrefixContainer
import kotlin.random.Random

group = "tablestakes"

repositories {
    jcenter()
}

plugins {
    kotlin("multiplatform")
    jacoco
    id("org.jetbrains.gradle.plugin.idea-ext")
    id("io.gitlab.arturbosch.detekt")
}

kotlin {
    jvm {
        val jvmTarget: String by project
        compilations.all {
            println("Setting kotlinOptions.jvmTarget to $jvmTarget")
            kotlinOptions.jvmTarget = jvmTarget
        }

        tasks {
            val jvmTest by getting(Test::class) {
                useJUnitPlatform {}
            }
        }
    }

    js {
        browser()
        nodejs()
    }

    mingwX64 {
        binaries.sharedLib()
    }

    linuxX64 {
        binaries.sharedLib()
    }


    sourceSets {

        val commonTest by getting {
            dependencies {
                implementation(kotlin("test-common"))
                implementation(kotlin("test-annotations-common"))
            }
        }

        val jvmTest by getting {
            dependencies {
                val junitVersion: String by project
                implementation(kotlin("test"))
                implementation(kotlin("test-junit5"))
                runtimeOnly("org.junit.jupiter:junit-jupiter-engine:$junitVersion")
            }
        }

        val jsTest by getting {
            dependencies {
                implementation(kotlin("test-js"))
            }
        }

        all {
            languageSettings.progressiveMode = true
            languageSettings.useExperimentalAnnotation("kotlin.RequiresOptIn")
        }
    }
}

tasks {
    withType<AbstractTestTask> {
        inputs.property("always.rerun", Random.nextLong())
        testLogging {
            showStandardStreams = true
            events = TestLogEvent.values().toSet() - TestLogEvent.STARTED
            showStackTraces = true
            showExceptions = true
            exceptionFormat = TestExceptionFormat.FULL
        }
    }

    getByName<io.gitlab.arturbosch.detekt.Detekt>("detekt") {
        val kotlinSourceDirectories = kotlin.sourceSets
            .filter {
                it.name.endsWith("Main")
            }.map { it.kotlin }
        println("Setting Detekt source directories to: $kotlinSourceDirectories")
        source(kotlinSourceDirectories)
        include("**/*.kt")
    }
}

// hopefully one day this will work for common source sets! :-/
idea {
    module {
        settings {
            val modulePrefix = "$group.$name"
            packagePrefix["src/commonMain/kotlin"] = modulePrefix
            packagePrefix["src/commonTest/kotlin"] = modulePrefix
        }
    }
}

fun IdeaModule.settings(configure: ModuleSettings.() -> Unit) =
    (this as ExtensionAware).configure(configure)

val ModuleSettings.packagePrefix: PackagePrefixContainer
    get() = (this as ExtensionAware).the()
