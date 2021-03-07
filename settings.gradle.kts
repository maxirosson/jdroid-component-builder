plugins {
    id("com.gradle.enterprise").version("3.5")
}

include(":dipien-component-builder")

apply(from = java.io.File(settingsDir, "buildCacheSettings.gradle"))
