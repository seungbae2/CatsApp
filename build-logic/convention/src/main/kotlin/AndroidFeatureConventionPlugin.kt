import com.example.catsapp.libs
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.apply
import org.gradle.kotlin.dsl.dependencies

class AndroidFeatureConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            pluginManager.apply {
                apply(plugin = "catsapp.android.library")
                apply(plugin = "catsapp.hilt")
                apply(plugin = "org.jetbrains.kotlin.plugin.serialization")
            }

            dependencies {
                "implementation"(libs.findLibrary("androidx.hilt.navigation.compose").get())
                "implementation"(libs.findLibrary("androidx.lifecycle.runtimeCompose").get())
                "implementation"(libs.findLibrary("androidx.lifecycle.viewModelCompose").get())
                "implementation"(libs.findLibrary("androidx.navigation.compose").get())
                "implementation"(libs.findLibrary("kotlinx.serialization.json").get())
                "implementation"(libs.findLibrary("timber").get())
            }
        }
    }
} 