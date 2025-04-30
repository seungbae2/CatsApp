plugins {
    alias(libs.plugins.catsapp.jvm.library)
    alias(libs.plugins.catsapp.hilt)
}

dependencies {
    implementation(libs.kotlinx.coroutines.core)
    testImplementation(libs.kotlinx.coroutines.test)
}