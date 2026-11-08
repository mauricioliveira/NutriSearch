plugins {
    alias(libs.plugins.kotlin.jvm)
}

kotlin {
    jvmToolchain(libs.versions.jvm.get().toInt())
}

dependencies {
    api(projects.core.domain)
    api(libs.junit)
    api(libs.kotlinx.coroutines.test)
}
