plugins {
    alias(libs.plugins.catsapp.android.library)
    alias(libs.plugins.catsapp.hilt)
}

android {
    namespace = "com.example.catsapp.core.domain"
}

dependencies {
    implementation(project(":core:common"))
} 