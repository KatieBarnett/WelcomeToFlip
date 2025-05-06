ext {
    extra["appVersionName"] = "1.1.0"
    extra["appVersionCode"] = 10
    extra["compileSdk"] = 35
    extra["targetSdk"] = 35
    extra["minSdk"] = 26
}

plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.android.library) apply false
    alias(libs.plugins.kotlin.android) apply false
    alias(libs.plugins.dagger.hilt.android) apply (false)
    alias(libs.plugins.protobuf) apply (false)
    alias(libs.plugins.firebase.crashlytics) apply (false)
    alias(libs.plugins.google.services) apply (false)
    alias(libs.plugins.ksp) apply (false)
    alias(libs.plugins.paparazzi) apply (false)
    alias(libs.plugins.compose.compiler) apply (false)
}
true // Needed to make the Suppress annotation work for the plugins block