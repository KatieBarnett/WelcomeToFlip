// Top-level build file where you can add configuration options common to all sub-projects/modules.
@Suppress("DSL_SCOPE_VIOLATION") // TODO: Remove once KTIJ-19369 is fixed
//buildscript {
//
//
//
//
//    dependencies {
//        classpath "com.google.dagger:hilt-android-gradle-plugin:$hilt_version"
//        classpath 'com.google.gms:google-services:4.3.15'
//        classpath 'com.google.firebase:firebase-crashlytics-gradle:2.9.2'
////        classpath "org.owasp:dependency-check-gradle:$dependency_check_version"
//    }
//}

ext {
    extra["appVersionName"] = "1.0.0"
    extra["compileSdk"] = 33
    extra["targetSdk"] = 33
    extra["minSdk"] = 26
}

plugins {
//    id 'org.jetbrains.kotlin.android' version "$kotlin_version" apply false
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.android.library) apply false
    alias(libs.plugins.kotlin.android) apply false
    alias(libs.plugins.dagger.hilt.android) apply (false)
    alias(libs.plugins.protobuf) apply (false)
    alias(libs.plugins.firebase.crashlytics) apply (false)
    alias(libs.plugins.google.services) apply (false)
}
true // Needed to make the Suppress annotation work for the plugins block