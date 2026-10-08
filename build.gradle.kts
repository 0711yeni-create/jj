plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
}

android {
    namespace = "com.example.jaroleplay"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.example.jaroleplay"
        minSdk = 24
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"
    }
}

kotlinOptions { jvmTarget = "17" }
