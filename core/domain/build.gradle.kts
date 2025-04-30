plugins {
    alias(libs.plugins.catsapp.android.library)
    alias(libs.plugins.catsapp.hilt)
}

android {
    namespace = "com.example.catsapp.core.domain"
}

dependencies {
    implementation(project(":core:common"))
    implementation(project(":core:data-api"))
    implementation(project(":core:model"))

    implementation(libs.androidx.paging.compose)
    implementation(libs.androidx.paging.runtime)
} 