plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.dagger.hilt.android)
    alias(libs.plugins.google.services)
    alias(libs.plugins.firebase.crashlytics)
    alias(libs.plugins.ksp)
    alias(libs.plugins.paparazzi)
    alias(libs.plugins.compose.compiler)
}

android {
    namespace = "dev.veryniche.welcometoflip"
    compileSdk = rootProject.extra["compileSdk"] as Int

    defaultConfig {
        applicationId = "dev.veryniche.welcometoflip"
        minSdk = rootProject.extra["minSdk"] as Int
        targetSdk = rootProject.extra["targetSdk"] as Int

        versionCode = rootProject.extra["appVersionCode"] as Int
        versionName = rootProject.extra["appVersionName"] as String

//        // Required because of https://commonsware.com/blog/2020/10/14/android-studio-4p1-library-modules-version-code.html
//        buildConfigField ('int', 'APP_VERSION_CODE', computeVersionCode().toString())
//        buildConfigField 'String', 'APP_VERSION_NAME', "\"$app_version_name\""
    }

    buildTypes {
        debug {
            versionNameSuffix = ".debug"
        }

        release {
            isMinifyEnabled = true
            isShrinkResources = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
            signingConfig = signingConfigs.getByName("debug")
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
        isCoreLibraryDesugaringEnabled = true
    }
    kotlinOptions {
        jvmTarget = "17"
    }
    buildFeatures {
        compose = true
        buildConfig = true
    }
    ksp {
        arg("skipPrivatePreviews", "true")
    }
}

dependencies {
    implementation(project(":storage"))
    implementation(project(":core"))

    coreLibraryDesugaring(libs.desugar.jdk.libs)

    implementation(libs.core)
    implementation(libs.hilt)

    implementation(libs.material)

    implementation(libs.billing)

    implementation(platform(libs.firebase.bom))
    implementation(libs.firebase.analytics)
    implementation(libs.firebase.crashlytics)

    implementation(platform(libs.compose.bom))
    implementation(libs.compose.foundation)
    implementation(libs.compose.material3)
    implementation(libs.compose.ui)
    implementation(libs.lifecycle.runtime.compose)
    implementation(libs.material3.windowsizeclass)
    implementation(libs.material3.adaptive)
    debugImplementation(libs.compose.ui.tooling)
    implementation(libs.compose.ui.tooling.preview)
    implementation(libs.compose.animation)
    implementation(libs.compose.runtime.livedata)
    implementation(libs.compose.material.iconscore)
    implementation(libs.compose.material.iconsext)

    implementation(libs.hilt.navigation.compose)
    implementation(libs.compose.navigation)

    implementation(libs.lifecycle.livedata)
    implementation(libs.lifecycle.viewmodel)
    implementation(libs.lifecycle.runtime)
    implementation(libs.lifecycle.viewmodel.compose)

    implementation(libs.splashscreen)

    implementation(libs.timber)
    
    implementation(libs.play.services.base)
    implementation(libs.play.services.ads)
    implementation(libs.play.review)
    implementation(libs.play.review.ktx)
    implementation(libs.play.app.update)
    implementation(libs.play.app.update.ktx)

    implementation(libs.showkase)
    ksp(libs.showkase.processor)

    ksp(libs.hilt.compiler)
}
