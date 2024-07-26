plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.jetbrains.kotlin.android)
    kotlin("kapt")
    id("dagger.hilt.android.plugin")
    alias(libs.plugins.kotlin.serialization)
}

android {
    namespace = "com.hitec.renewsmartset"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.hitec.renewsmartset"
        minSdk = 26
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions {
        jvmTarget = "17"
    }
    buildFeatures {
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.1"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)

    implementation(project(":domain"))
    implementation(project(":data"))
    implementation(project(":presentation"))

    implementation(libs.google.hilt)
    kapt(libs.google.hilt.compiler)

//    implementation(libs.androidx.room.runtime)
//    implementation(libs.androidx.room.ktx)
//    kapt(libs.androidx.room.compiler)
//    implementation(libs.google.gson)
//
//    implementation(libs.retrofit)
//    implementation(libs.retrofit.converter.scalars)
//    implementation(libs.retrofit.converter.serialization)
    implementation(libs.kotlinx.serialization.json)
//    implementation(libs.okhttp)
//
//    implementation(libs.tikxml.core)
//    implementation(libs.tikxml.annotation)
//    implementation(libs.tikxml.retrofit.converter)
//    kapt(libs.tikxml.processor)
}

kapt {
    correctErrorTypes = true
}