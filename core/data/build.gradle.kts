plugins {
    alias(libs.plugins.catsapp.android.library)
    alias(libs.plugins.catsapp.hilt)
    id("kotlinx-serialization")
}

android {
    namespace = "com.example.catsapp.core.data"
}

dependencies {
    implementation(project(":core:domain"))
    implementation(project(":core:network"))
    implementation(project(":core:database"))
    implementation(project(":core:common"))
    
    implementation(libs.androidx.paging.runtime)
    implementation(libs.kotlinx.serialization.json)
    implementation(libs.androidx.paging.runtime)
    implementation(libs.androidx.paging.compose)
} 