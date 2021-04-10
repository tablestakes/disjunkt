@file:Suppress("UNUSED_VARIABLE")

import io.gitlab.arturbosch.detekt.Detekt
import org.gradle.api.tasks.testing.logging.TestExceptionFormat
import org.gradle.api.tasks.testing.logging.TestLogEvent
import org.gradle.plugins.ide.idea.model.IdeaModule
import org.jetbrains.dokka.gradle.DokkaTask
import org.jetbrains.gradle.ext.ModuleSettings
import org.jetbrains.gradle.ext.PackagePrefixContainer
import org.jetbrains.kotlin.gradle.plugin.KotlinSourceSet
import kotlin.random.Random

group = "io.github.tablestakes"

repositories {
    mavenCentral()
    jcenter()
}

plugins {
    kotlin("multiplatform")
//    id("de.otto.find.project-version")
    jacoco
    id("org.jetbrains.gradle.plugin.idea-ext")
    id("io.gitlab.arturbosch.detekt")
    id("org.jetbrains.dokka")
    signing
    `maven-publish`
}

//projectVersion {
//    useSemanticVersioning() {
//        majorVersion = 0
//    }
//}

kotlin {

    targets.all {
        compilations.all {
            kotlinOptions {
                languageVersion = "1.4"
            }
        }
    }
    jvm {
        val kotlinJvmTarget: String by project
        compilations.all {
            println("Setting kotlinOptions.jvmTarget to $kotlinJvmTarget")
            kotlinOptions {
                jvmTarget = kotlinJvmTarget
            }
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
            packagePrefix("$group.$name")
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

    getByName<Detekt>("detekt") {
        val kotlinSourceDirectories = kotlin.sourceSets
            .filter {
                it.name.endsWith("Main")
            }.map { it.kotlin }
        logger.info("Setting Detekt source directories to: $kotlinSourceDirectories")
        source(kotlinSourceDirectories)
        include("**/*.kt")
    }
}

val dokkaHtml by tasks.getting(DokkaTask::class)

val javadocJar: TaskProvider<Jar> by tasks.registering(Jar::class) {
    archiveClassifier.set("javadoc")
    from(dokkaHtml)
}

publishing {
    repositories {
        mavenLocal()

        onlyInCi {
            maven {
                name = "MavenCentral"
                url = uri("https://s01.oss.sonatype.org/")
                credentials(PasswordCredentials::class)
            }
        }
    }

    publications.withType<MavenPublication> {
        artifact(javadocJar)

        pom {
            name.set(project.name)
            description.set("A right-biased, monadic disjunction for Kotlin multiplatform.")
            url.set("https://github.com/tablestakes/disjunkt")

            scm {
                val projectGitUrl = "https://github.com/tablestakes/disjunkt.git"
                connection.set("scm:git:$projectGitUrl.git")
                developerConnection.set("scm:git:$projectGitUrl.git")
                url.set(projectGitUrl)
            }

            licenses {
                license {
                    name.set("The Apache License, Version 2.0")
                    url.set("https://www.apache.org/licenses/LICENSE-2.0.txt")
                }
            }

            developers {
                developer {
                    name.set("Aaron Knauf")
                    email.set("aknauf+tablestakes@gmail.com")
                    url.set("https://github.com/aknauf")
                }
            }
        }
    }
}

onlyInCi {
    signing {
        val signingKey: String? by project
        val signingPassword: String? by project
        useInMemoryPgpKeys(signingKey, signingPassword)
        sign(publishing.publications)
    }
}

// tidy up gradle 7 implicit dependency warnings.
tasks {
    val signTasks = withType<Sign>()
    withType<PublishToMavenRepository> {
        dependsOn(check, signTasks)
    }

    listOf(
        "jsSourcesJar" to "jsGenerateExternalsIntegrated",
        "detekt" to "jsGenerateExternalsIntegrated"
    ).map { (t, d) -> getByName(t) to getByName(d) }.forEach { (t, d) ->
        t.dependsOn(d)
    }
}

// hopefully one day this will work for common source sets! :-/
fun KotlinSourceSet.packagePrefix(prefix: String) {
    idea.module.settings {
        this@packagePrefix.kotlin.sourceDirectories
            .map { f -> f.toRelativeString(projectDir) }
            .forEach {
                packagePrefix[it] = prefix
            }
    }
}

fun IdeaModule.settings(configure: ModuleSettings.() -> Unit) =
    (this as ExtensionAware).configure(configure)

val ModuleSettings.packagePrefix: PackagePrefixContainer
    get() = (this as ExtensionAware).the()

fun onlyInCi(block: () -> Unit) = System.getenv("CI")?.let { block() }
