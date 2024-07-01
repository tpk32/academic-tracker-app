plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.jetbrains.kotlin.android)
    id("kotlin-kapt")
    alias(libs.plugins.google.gms.google.services)

}

android {
    namespace = "com.tpkprojects.academictracker"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.tpkprojects.academictracker"
        minSdk = 23
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
        isCoreLibraryDesugaringEnabled = true
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
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
    //Compose ViewModel
    implementation("androidx.lifecycle:lifecycle-viewmodel-compose:2.6.2")

    //Network calls
    implementation("com.squareup.retrofit2:retrofit:2.9.0")

    //Json to Kotlin object mapping
    implementation("com.squareup.retrofit2:converter-gson:2.9.0")

    //Image loading
    implementation("io.coil-kt:coil-compose:2.4.0")

    implementation(libs.androidx.graphics.shapes.android)
    implementation ("androidx.constraintlayout:constraintlayout-compose:1.0.1")
    implementation("com.google.firebase:firebase-auth")
    implementation("com.google.android.gms:play-services-auth:21.2.0")
    implementation(libs.firebase.auth)
    implementation(libs.androidx.lifecycle.runtime.compose.android)
    val room_version = "2.6.1"
    val nav_version = "2.7.5"

    implementation("androidx.room:room-runtime:$room_version")
    annotationProcessor("androidx.room:room-compiler:$room_version")
    implementation("androidx.room:room-ktx:$room_version")
    // To use Kotlin annotation processing tool (kapt)
    kapt("androidx.room:room-compiler:$room_version")
    // To use Kotlin Symbol Processing (KSP)
    //ksp("androidx.room:room-compiler:$room_version")
    implementation("androidx.compose.material:material:1.6.7")
    implementation("androidx.compose.ui:ui:1.6.8")
    implementation("androidx.navigation:navigation-compose:$nav_version")
    implementation ("androidx.compose.material3:material3:1.0.0-alpha02")
    //date-picker
    implementation("io.github.vanpra.compose-material-dialogs:datetime:0.8.1-rc")
    //use local day time api of java
    coreLibraryDesugaring("com.android.tools:desugar_jdk_libs:1.1.6")
    implementation ("androidx.graphics:graphics-shapes:1.0.0-alpha05")

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

}