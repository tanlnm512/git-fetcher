plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.parcelize)
    alias(libs.plugins.ksp)
    alias(libs.plugins.navigation.safe.args)
}

android {
    namespace = "io.tanlnm.gitfetcher"
    compileSdk = 35

    defaultConfig {
        applicationId = "io.tanlnm.gitfetcher"
        minSdk = 27
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        buildConfigField("String", "API_URL", "\"https://api.github.com/\"")
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
        jvmTarget = JavaVersion.VERSION_17.toString()
    }

    buildFeatures {
        viewBinding = true
        buildConfig = true
    }

    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

dependencies {
    implementation(libs.bundles.android.extensions)
    implementation(libs.bundles.android.support)
    implementation(libs.bundles.lifecycle)

    implementation(platform(libs.koin.bom))
    implementation(libs.bundles.koin)

    implementation(libs.work.runtime)

    implementation(libs.bundles.room)
    ksp(libs.room.compiler)

    implementation(libs.bundles.navigation)

    implementation(platform(libs.okhttp.bom))
    implementation(libs.bundles.network)

    implementation(libs.coil)
    implementation(libs.coil.network)

    implementation(libs.gson)
    implementation(libs.timber)

    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.test.ext.junit)
    androidTestImplementation(libs.espresso.core)
}