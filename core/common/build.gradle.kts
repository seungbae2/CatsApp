plugins {
    alias(libs.plugins.catsapp.android.library)
    alias(libs.plugins.catsapp.hilt)
}

android {
    namespace = "com.example.catsapp.core.common"
}

dependencies {
    implementation(libs.kotlinx.coroutines.core)
    implementation(libs.androidx.lifecycle.process)
    testImplementation(libs.kotlinx.coroutines.test)
}