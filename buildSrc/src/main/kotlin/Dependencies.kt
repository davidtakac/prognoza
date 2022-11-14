// These versions are extracted like this because they need to
// be used in the project level build.gradle.kts as well as in the
// regular dependencies below
object GlobalVersions {
    const val kotlin = "1.7.10"
    // Tied to Kotlin version, see link for compatibility map
    // https://developer.android.com/jetpack/androidx/releases/compose-kotlin#pre-release_kotlin_compatibility
    const val kotlinCompilerExtensionVersion = "1.3.1"
    const val sqlDelight = "1.5.3"
    const val gradle = "7.2.2"
    const val hilt = "2.43.2"
}

object AndroidConfig {
    const val minSdk = 26
    const val compileSdk = 33
    const val targetSdk = compileSdk
    const val jvmTarget = "11"
}

object Dependencies {
    const val jUnit = "junit:junit:4.13"
    const val napier = "io.github.aakira:napier:2.6.1"

    object Ktor {
        private const val version = "2.1.3"
        const val core = "io.ktor:ktor-client-core:$version"
        const val cio = "io.ktor:ktor-client-cio:$version"
        const val logging = "io.ktor:ktor-client-logging:$version"
        const val contentNegotiation = "io.ktor:ktor-client-content-negotiation:$version"
        const val serialization = "io.ktor:ktor-serialization-kotlinx-json:$version"
    }

    object SqlDelight {
        private const val version = GlobalVersions.sqlDelight
        const val core = "com.squareup.sqldelight:runtime:$version"
        const val android = "com.squareup.sqldelight:android-driver:$version"
    }

    object Coroutines {
        private const val version = "1.6.1"
        const val android = "org.jetbrains.kotlinx:kotlinx-coroutines-android:$version"
        const val core = "org.jetbrains.kotlinx:kotlinx-coroutines-core:$version"
    }

    object DateTime {
        private const val version = "0.4.0"
        const val jvm = "org.jetbrains.kotlinx:kotlinx-datetime-jvm:$version"
        const val core = "org.jetbrains.kotlinx:kotlinx-datetime:$version"
    }

    object Android {
        const val splashScreen = "androidx.core:core-splashscreen:1.0.0"
        const val work = "androidx.work:work-runtime-ktx:2.7.1"
        const val glance = "androidx.glance:glance-appwidget:1.0.0-alpha05"

        object Hilt {
            private const val version = GlobalVersions.hilt
            const val core = "com.google.dagger:hilt-android:$version"
            const val kapt = "com.google.dagger:hilt-compiler:$version"
            const val composeNavigation = "androidx.hilt:hilt-navigation-compose:1.0.0"
        }

        object Lifecycle {
            private const val version = "2.5.1"
            const val core = "androidx.lifecycle:lifecycle-runtime-ktx:$version"
            const val viewModel = "androidx.lifecycle:lifecycle-viewmodel-ktx:$version"
            const val composeViewModel = "androidx.lifecycle:lifecycle-viewmodel-compose:$version"
        }

        object Compose {
            private const val version = "1.3.0"
            const val core = "androidx.compose.ui:ui:$version"
            const val animation = "androidx.compose.animation:animation:$version"
            const val tooling = "androidx.compose.ui:ui-tooling:$version"
            const val testJUnit4 = "androidx.compose.ui:ui-test-junit4:$version"
            const val testManifest = "androidx.compose.ui:ui-test-manifest:$version"

            const val activity = "androidx.activity:activity-compose:1.6.1"
            const val material3 = "androidx.compose.material3:material3:1.0.0"
            const val navigation = "androidx.navigation:navigation-compose:2.5.3"
            const val coil = "io.coil-kt:coil-compose:2.1.0"
            const val systemUiController = "com.google.accompanist:accompanist-systemuicontroller:0.27.0"
        }

        object Test {
            private const val version = "1.4.0"
            const val core = "androidx.test:core:$version"
            const val runner = "androidx.test:runner:$version"
            const val rules = "androidx.test:rules:$version"
            const val androidJUnit = "androidx.test.ext:junit:1.1.2"
            const val espresso = "androidx.test.espresso:espresso-core:3.4.0"
        }
    }
}