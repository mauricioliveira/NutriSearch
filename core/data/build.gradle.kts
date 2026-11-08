plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.serialization)
}

android {
    namespace = "com.mauricioliveira.nutrisearch.core.data"
    compileSdk = libs.versions.compileSdk.get().toInt()
    defaultConfig {
        minSdk = libs.versions.minSdk.get().toInt()
        buildConfigField("String", "API_BASE_URL", "\"https://nutrisearch.vercel.app/\"")
    }
    buildFeatures {
        buildConfig = true
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
    api(projects.core.domain)
    implementation(projects.core.database)
    implementation(libs.bundles.ktor)
    implementation(libs.kotlinx.serialization.json)
    implementation(libs.androidx.room.paging)
    implementation(platform(libs.koin.bom))
    implementation(libs.koin.core)

    testImplementation(projects.core.testing)
    testImplementation(libs.ktor.client.mock)
    testImplementation(libs.androidx.paging.testing)
    testImplementation(kotlin("test"))
}
