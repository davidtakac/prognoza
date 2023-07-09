import java.io.FileInputStream
import java.util.*

plugins {
  id("com.android.application")
  id("dagger.hilt.android.plugin")
  kotlin("android")
  kotlin("kapt")
  kotlin("plugin.serialization")
}

android {
  namespace = "hr.dtakac.prognoza"
  compileSdk = AndroidConfig.compileSdk
  defaultConfig {
    applicationId = "hr.dtakac.prognoza"
    minSdk = AndroidConfig.minSdk
    targetSdk = AndroidConfig.targetSdk
    versionCode = 20
    // Before pushing to release, make sure this value matches the latest one in CHANGELOG.md
    versionName = "3.4.1"
    testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    resourceConfigurations.addAll(listOf("en", "hr", "de", "ru"))
  }

  signingConfigs {
    /**
     * To make local release builds work, you have to:
     *  1. Place keystore.jks in androidApp/
     *  2. Place keystore.properties in androidApp/
     *  3. Its content should be of the format:
     *     storeFile=keystore.jks
     *     storePassword=my_store_password
     *     keyAlias=my_key_alias
     *     keyPassword=my_keystore_password
     *
     *  You can also choose to ignore this and just use debug builds.
     */
    val keystorePropsFile = file("keystore.properties")
    if (!keystorePropsFile.isFile) return@signingConfigs

    val keystoreProps = Properties()
    keystoreProps.load(FileInputStream(keystorePropsFile))
    val keystoreFile = file(keystoreProps.getProperty("storeFile"))
    if (!keystoreFile.isFile) return@signingConfigs

    create("release") {
      storeFile = keystoreFile
      storePassword = keystoreProps.getProperty("storePassword")
      keyAlias = keystoreProps.getProperty("keyAlias")
      keyPassword = keystoreProps.getProperty("keyPassword")
    }
  }

  buildTypes {
    getByName("release") {
      isMinifyEnabled = true
      isShrinkResources = true
      isDebuggable = false
      proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
      // CI has a different way of signing the release, see
      // .github/workflows/build_and_upload_to_github.yml
      if (System.getenv("CI") == null) {
        try {
          signingConfig = signingConfigs.getByName("release")
        } catch (_: UnknownDomainObjectException) {
          // Ignore missing signingConfig, allowing Gradle to sync.
          // See the signingConfigs block above for more info.
        }
      }
    }
    getByName("debug") {
      isMinifyEnabled = false
      isDebuggable = true
      applicationIdSuffix = ".debug"
    }
  }
  compileOptions {
    sourceCompatibility = JavaVersion.VERSION_11
    targetCompatibility = JavaVersion.VERSION_11
  }
  buildFeatures {
    compose = true
  }
  composeOptions {
    kotlinCompilerExtensionVersion = GlobalVersions.kotlinCompilerExtensionVersion
  }
  kotlinOptions {
    jvmTarget = AndroidConfig.jvmTarget
  }
}

dependencies {
  implementation(project(":shared"))

  implementation(Dependencies.napier)
  implementation(Dependencies.Serialization.json)
  implementation(Dependencies.Coroutines.android)
  implementation(Dependencies.DateTime.jvm)

  implementation(Dependencies.Android.Hilt.composeNavigation)
  implementation(Dependencies.Android.Hilt.core)
  kapt(Dependencies.Android.Hilt.kapt)

  implementation(Dependencies.Android.core)
  implementation(Dependencies.Android.appcompat)
  implementation(Dependencies.Android.work)
  implementation(Dependencies.Android.splashScreen)
  implementation(Dependencies.Android.Lifecycle.core)
  implementation(Dependencies.Android.Lifecycle.viewModel)
  implementation(Dependencies.Android.Lifecycle.composeViewModel)
  implementation(Dependencies.Android.Compose.core)
  implementation(Dependencies.Android.Compose.activity)
  implementation(Dependencies.Android.Compose.material3)
  implementation(Dependencies.Android.Compose.animation)
  implementation(Dependencies.Android.Compose.tooling)
  implementation(Dependencies.Android.Compose.navigation)
  implementation(Dependencies.Android.Compose.coil)
  implementation(Dependencies.Android.Compose.systemUiController)

  androidTestImplementation(Dependencies.jUnit)
  androidTestImplementation(Dependencies.Android.Compose.testJUnit4)
  debugImplementation(Dependencies.Android.Compose.testManifest)
  androidTestImplementation(Dependencies.Android.Test.androidJUnit)
  androidTestImplementation(Dependencies.Android.Test.core)
  androidTestImplementation(Dependencies.Android.Test.espresso)
  androidTestImplementation(Dependencies.Android.Test.espresso)
  androidTestImplementation(Dependencies.Android.Test.rules)
}