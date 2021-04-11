[![codebeat badge](https://codebeat.co/badges/3eea2673-85ed-4e76-98c9-e4281d3aca0e)](https://codebeat.co/projects/github-com-tablestakes-disjunkt-master)
[![download](https://img.shields.io/maven-central/v/io.github.tablestakes/disjunkt)](https://mvnrepository.com/artifact/io.github.tablestakes/disjunkt)

# Disjunkt

A right-biased, monadic disjunction for Kotlin multiplatform.

Largely drawn from libs like arrow, result4k and Kotlin's own result, but multiplatform, applicable beyond
success/failure and accompanied by a handy set of extension functions.

## Installation

### Kotlin JVM

```kotlin
kotlin {
    sourceSets {
        main {
            dependencies{
                implementation("io.github.tablestakes:disjunkt:0.1.1")
            }
        }
    }
}
```

### Kotlin Multiplatform

Currently built for `jvm (11)`, `linux_x64`, `js` platform targets. All code is Kotlin common, so if you want to see
support for additional platforms, feel free to raise an issue/pull request.

```kotlin
kotlin {
    sourceSets {
        commonMain {
            dependencies {
                implementation("io.github.tablestakes:disjunkt:0.1.1")
            }
        }
    }
}
```

