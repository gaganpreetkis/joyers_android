plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.kotlin.ksp)
    alias(libs.plugins.hilt)

//    inside libs.versions.tomlput this also after opening this section
//    hilt = { id = "com.google.dagger.hilt.android", version.ref = "hilt" }
//    kotlin-ksp = { id = "com.google.devtools.ksp", version = "2.2.10-2.0.2" }
}

val javapoetCoordinate = "com.squareup:javapoet:${libs.versions.javapoet.get()}"

configurations.all {
    resolutionStrategy.force(javapoetCoordinate)
}

android {
    namespace = "com.joyersapp"
    compileSdk {
        version = release(36)
    }

    defaultConfig {
        applicationId = "com.joyersapp"
        minSdk = 30
        targetSdk = 36
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = "11"
    }
    buildFeatures {
        compose = true
    }
}

dependencies {
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(libs.androidx.navigation.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.compose.ui)
    implementation(libs.androidx.compose.ui.graphics)
    implementation(libs.androidx.compose.ui.tooling.preview)
    implementation(libs.androidx.compose.material3)
    implementation(libs.androidx.datastore.core)
    implementation(libs.androidx.datastore.preferences.core)
    implementation(libs.androidx.compose.testing)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.compose.ui.test.junit4)
    debugImplementation(libs.androidx.compose.ui.tooling)
    debugImplementation(libs.androidx.compose.ui.test.manifest)

    implementation(libs.ccp)
    implementation(libs.coil.compose)
    implementation(libs.coil.gif)


    //retrofit and okhttp
    implementation(libs.retrofit)
    implementation(libs.converter.gson)
    implementation(libs.logging.interceptor)
    implementation(libs.okhttp)
    implementation(libs.okhttp.urlconnection)

    // ViewModel & Lifecycle (REQUIRED)
    implementation(libs.androidx.lifecycle.viewmodel.compose.v2100)
    implementation(libs.androidx.lifecycle.runtime.compose.v2100)

    // Coroutines (REQUIRED)
    implementation(libs.kotlinx.coroutines.android.v1102)

    // Hilt
    implementation(libs.hilt.android)
    ksp(libs.hilt.compiler)
    implementation(libs.hilt.navigation.compose)
    implementation(libs.javapoet)
}