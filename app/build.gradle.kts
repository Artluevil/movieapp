plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.hilt) // ✅ Hilt with KSP
    alias(libs.plugins.ksp)  // ✅ KSP enabled
}

android {
    namespace = "com.kbak.moviesapp"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.kbak.moviesapp"
        minSdk = 31
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        buildConfigField("String", "TMDB_API_KEY", "\"${project.findProperty("TMDB_API_KEY")}\"")
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
        buildConfig = true
    }
}

dependencies {
    // Hilt Dependencies (Now using KSP)
    implementation(libs.hilt.android)
    implementation(libs.androidx.adapters)
    ksp(libs.hilt.compiler)

    // Room Dependencies (KSP instead of KAPT)
    implementation(libs.androidx.room.runtime)
    implementation(libs.androidx.room.ktx)
    ksp(libs.androidx.room.compiler) // ✅ Use KSP for Room

    // Hilt Navigation (For Jetpack Compose)
    implementation(libs.hilt.navigation.compose)

    // Android Core
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)

    // Jetpack Compose
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)

    // Retrofit (API Calls)
    implementation("com.squareup.retrofit2:retrofit:2.9.0")
    implementation("com.squareup.retrofit2:converter-gson:2.9.0")

    // OkHttp (Networking)
    implementation("com.squareup.okhttp3:okhttp:4.11.0")
    implementation("com.squareup.okhttp3:logging-interceptor:4.11.0")

    // Coil for Image Loading
    implementation("io.coil-kt:coil-compose:2.4.0")

    // Navigation
    implementation("androidx.navigation:navigation-compose:2.7.5")

    //splashscreen
    implementation("androidx.core:core-splashscreen:1.2.0-beta01")

    // Testing Dependencies
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)
}

ksp {
    arg("room.schemaLocation", "$projectDir/schemas") // ✅ Tells Room where to store schema history
    arg("room.incremental", "true") // ✅ Improves compilation speed
    arg("room.expandProjection", "true") // ✅ Prevents some runtime crashes
}
