plugins {
    alias(libs.plugins.catsapp.jvm.library)
    id("kotlinx-serialization")
}

dependencies {
    implementation(libs.kotlinx.serialization.json)
}
