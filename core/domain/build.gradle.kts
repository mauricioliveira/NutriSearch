plugins {
    alias(libs.plugins.kotlin.jvm)
}

kotlin {
    jvmToolchain(libs.versions.jvm.get().toInt())
}

dependencies {
    // paging-common is a pure Kotlin artifact, so PagingData can cross the domain boundary
    // without pulling Android into this module.
    api(libs.androidx.paging.common)
    api(libs.kotlinx.coroutines.core)
}
