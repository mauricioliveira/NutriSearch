plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.kotlin.serialization)
}

android {
    namespace = "com.mauricioliveira.nutrisearch.feature.professionals"
    compileSdk = libs.versions.compileSdk.get().toInt()
    defaultConfig {
        minSdk = libs.versions.minSdk.get().toInt()
    }
    buildFeatures {
        compose = true
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
}

kotlin {
    jvmToolchain(libs.versions.jvm.get().toInt())
}

dependencies {
    implementation(projects.core.domain)
    implementation(projects.core.ui)
    debugImplementation(libs.androidx.compose.ui.tooling)
    implementation(platform(libs.koin.bom))
    implementation(libs.bundles.feature)
    implementation(libs.androidx.paging.compose)
    testImplementation(libs.androidx.paging.testing)

    testImplementation(projects.core.testing)
}
