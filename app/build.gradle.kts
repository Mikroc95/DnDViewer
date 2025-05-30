plugins {
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.jetbrainsKotlinAndroid)
    alias(libs.plugins.compose.compiler)
    id("kotlin-kapt")
    alias(libs.plugins.kotlinSerialization) // Si lo usas
    alias(libs.plugins.hilt) // Si lo usas
}

android {
    namespace = "com.mikroc.dndviewer"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.mikroc.dndviewer"
        minSdk = 26
        targetSdk = 34
        versionCode = 11
        versionName = "1.10"
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
        kotlinCompilerExtensionVersion = libs.versions.composeCompiler.get()//"1.5.14"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

configurations.all {
    resolutionStrategy {
        force("org.jetbrains.kotlin:kotlin-stdlib:${libs.versions.kotlin.get()}")
        force("org.jetbrains.kotlin:kotlin-stdlib-jdk8:${libs.versions.kotlin.get()}") // Por si acaso
        force("org.jetbrains.kotlin:kotlin-reflect:${libs.versions.kotlin.get()}")
    }
}

dependencies {
    implementation(libs.androidx.core.ktx)
    implementation(libs.hilt.android)
    kapt(libs.hilt.compiler)
    implementation(libs.kotlinx.coroutines.core)
    implementation(libs.kotlinx.coroutines.android)
    implementation("org.jetbrains.kotlin:kotlin-stdlib:${libs.versions.kotlin.get()}")
    constraints {
        implementation("org.jetbrains.kotlin:kotlin-stdlib:${libs.versions.kotlin.get()}") {
            because("Asegurar la compatibilidad de la versión de Kotlin stdlib con el compilador")
        }
        implementation("org.jetbrains.kotlin:kotlin-reflect:${libs.versions.kotlin.get()}") {
            because("Asegurar la compatibilidad de la versión de Kotlin reflect con el compilador")
        }
    }
    implementation("org.jetbrains.kotlin:kotlin-stdlib:${libs.versions.kotlin.get()}")
    // ROOM:
    implementation(libs.androidx.room.runtime)
    implementation(libs.androidx.room.ktx)
    kapt(libs.androidx.room.compiler)


     implementation("io.github.veselyjan92:pdfviewer-pdfium:1.0.5")
    implementation("io.coil-kt:coil-compose:2.6.0")

     implementation(libs.androidx.lifecycle.runtime.ktx)
     implementation(libs.androidx.activity.compose)
     implementation(platform(libs.androidx.compose.bom))
     implementation(libs.androidx.ui)
     implementation(libs.androidx.ui.graphics)
     implementation(libs.androidx.ui.tooling.preview)
     implementation(libs.androidx.material3)
     implementation(libs.androidx.navigation.runtime.ktx)
     implementation(libs.androidx.navigation.fragment.ktx)
     implementation(libs.androidx.navigation.compose)
     implementation(libs.androidx.ui.viewbinding)
     testImplementation(libs.junit)
     androidTestImplementation(libs.androidx.junit)
     androidTestImplementation(libs.androidx.espresso.core)
     androidTestImplementation(platform(libs.androidx.compose.bom))
     androidTestImplementation(libs.androidx.ui.test.junit4)
     debugImplementation(libs.androidx.ui.tooling)
     debugImplementation(libs.androidx.ui.test.manifest)





     val fragmentVersion = "1.7.1"

     // Java language implementation
     implementation("androidx.fragment:fragment:$fragmentVersion")
     // Kotlin
     implementation("androidx.fragment:fragment-ktx:$fragmentVersion")
     // Compose
     implementation("androidx.fragment:fragment-compose:$fragmentVersion")
     // Testing Fragments in Isolation
     debugImplementation("androidx.fragment:fragment-testing:$fragmentVersion")

     //BottomBar
     implementation("androidx.compose.material:material:1.7.8")
     //navigation
     implementation("androidx.navigation:navigation-compose:2.8.7")

     implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.6.3")



}