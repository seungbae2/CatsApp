plugins {
    alias(libs.plugins.catsapp.android.library)
    alias(libs.plugins.catsapp.hilt)
    id("kotlinx-serialization")
}

android {
    namespace = "com.example.catsapp.core.network"

    buildFeatures {
        buildConfig = true
    }
}

dependencies {
    implementation(project(":core:common"))

    implementation(libs.kotlinx.datetime)
    implementation(libs.coil.kt)
    implementation(libs.coil.kt.svg)
    api(libs.okhttp.logging)
    api(libs.retrofit.core)
    api(libs.retrofit.kotlin.serialization)
    implementation(libs.sandwich)
    implementation(libs.kotlinx.serialization.json)
    implementation(libs.kotlinx.coroutines.android)

    testImplementation(libs.kotlinx.coroutines.test)
} 