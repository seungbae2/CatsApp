plugins {
    alias(libs.plugins.catsapp.android.feature)
    alias(libs.plugins.catsapp.android.library.compose)
    alias(libs.plugins.catsapp.hilt)
}

android {
    namespace = "com.example.catsapp.feature.cats"
}

dependencies {
} 