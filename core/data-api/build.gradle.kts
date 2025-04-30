plugins {
    alias(libs.plugins.catsapp.android.library)
    id("kotlinx-serialization")
}

android {
    namespace = "com.example.catsapp.core.data_api"
}

dependencies {
    implementation(project(":core:common"))
    implementation(project(":core:model"))

    implementation(libs.kotlinx.coroutines.core)
    implementation(libs.androidx.paging.compose)
    implementation(libs.androidx.paging.runtime)
}