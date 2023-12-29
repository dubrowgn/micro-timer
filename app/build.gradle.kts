import java.io.ByteArrayOutputStream
import java.util.Locale

plugins {
    id("com.android.application")
    id("com.google.devtools.ksp")
    id("org.jetbrains.kotlin.android")
}

val version = 6
val isDebug = project.gradle.startParameter.taskNames.any {
    name -> name.lowercase(Locale.getDefault()).contains("debug")
}

fun gitHash(): String {
    val stdout = ByteArrayOutputStream()
    exec {
        commandLine("git", "rev-parse", "--short", "HEAD")
        standardOutput = stdout
    }

    return stdout.toString().trim()
}

fun versionString(): String {
    return if (isDebug) {
        "1.$version (${gitHash()})"
    } else {
        "1.$version"
    }
}

android {
    compileSdk = 34

    defaultConfig {
        applicationId = "dubrowgn.microtimer"
        minSdk = 28 // Handler.postDelayed()
        targetSdk = 34
        versionCode = version
        versionName = versionString()
    }

    buildTypes {
        release {
            isMinifyEnabled = true
            isShrinkResources = true
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions {
        jvmTarget = "17"
    }
    namespace = "dubrowgn.microtimer"
}

dependencies {
    val roomVersion = "2.6.1"
    implementation("androidx.room:room-runtime:$roomVersion")
    annotationProcessor("androidx.room:room-compiler:$roomVersion")
    ksp("androidx.room:room-compiler:$roomVersion")

    implementation("androidx.core:core-ktx:1.12.0")
}
