plugins {
    alias(libs.plugins.catsapp.android.library)
    alias(libs.plugins.catsapp.hilt)
    alias(libs.plugins.catsapp.android.room)
    id("kotlinx-serialization")
}

android {
    namespace = "com.example.catsapp.core.data"
}

dependencies {
    implementation(project(":core:network"))
    implementation(project(":core:database"))
    implementation(project(":core:common"))
    implementation(project(":core:model"))
    implementation(project(":core:data-api"))
    
    implementation(libs.kotlinx.serialization.json)
    implementation(libs.androidx.paging.runtime)
    implementation(libs.androidx.paging.compose)
} 