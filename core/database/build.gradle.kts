plugins {
    alias(libs.plugins.catsapp.android.library)
    alias(libs.plugins.catsapp.hilt)
    alias(libs.plugins.catsapp.android.room)
}

android {
    namespace = "com.example.catsapp.core.database"
}

dependencies {
    implementation(project(":core:common"))
} 