plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    id("kotlin-kapt")
    id("com.google.dagger.hilt.android")
    id("dagger.hilt.android.plugin")
    id("kotlin-parcelize")
    id("com.google.devtools.ksp")
}

android {
    namespace = "com.wavez.trackerwater"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.wavez.trackerwater"
        minSdk = 24
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro"
            )
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
        viewBinding = true
    }

}

dependencies {
    implementation("com.cheonjaeung.powerwheelpicker.android:powerwheelpicker:1.0.0")
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.androidx.espresso.core)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    implementation(project(":base_common"))
    implementation(libs.mpandroidchart)
    implementation(libs.androidx.room.runtime)
    ksp(libs.androidx.room.compiler)
    implementation(libs.androidx.room.ktx)

    implementation(libs.androidx.lifecycle.viewmodel.ktx)
    implementation(libs.androidx.lifecycle.livedata.ktx)
    ksp(libs.hilt.compiler)
    implementation(libs.hilt.android.v253)
    implementation(libs.androidx.activity.ktx)
    ksp(libs.androidx.hilt.compiler)
//    implementation (libs.sonic.water.wave.animation)
    //google
//    implementation ("androidx.lifecycle:lifecycle-viewmodel-ktx:2.7.0")
//    implementation ("androidx.lifecycle:lifecycle-livedata-ktx:2.7.0")
//    implementation ("com.google.dagger:hilt-android:2.48.1")
//    implementation ("androidx.lifecycle:lifecycle-process:2.7.0")
//    kapt ("com.google.dagger:hilt-compiler:2.48.1")
    implementation(libs.androidx.fragment.ktx)

}