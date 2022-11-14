plugins {
    id("com.android.application").version(GlobalVersions.gradle).apply(false)
    id("com.android.library").version(GlobalVersions.gradle).apply(false)
    id("com.squareup.sqldelight").version(GlobalVersions.sqlDelight).apply(false)
    id("com.google.dagger.hilt.android").version(GlobalVersions.hilt).apply(false)
    kotlin("android").version(GlobalVersions.kotlin).apply(false)
    kotlin("multiplatform").version(GlobalVersions.kotlin).apply(false)
    kotlin("plugin.serialization").version(GlobalVersions.kotlin).apply(false)
}

buildscript {
    repositories {
        gradlePluginPortal()
        google()
        mavenCentral()
    }
}

allprojects {
    repositories {
        google()
        mavenCentral()
    }
}