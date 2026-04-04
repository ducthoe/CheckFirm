import java.io.FileInputStream
import java.util.Properties

val keystorePropertiesFile = rootProject.file("keystore.properties")
val keystoreProperties = Properties()
keystoreProperties.load(FileInputStream(keystorePropertiesFile))

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.androidx.room)
    alias(libs.plugins.google.gms)
    alias(libs.plugins.google.hilt)
    alias(libs.plugins.google.ksp)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.kotlin.parcelize)
    alias(libs.plugins.kotlin.serialization)
}

android {
    namespace = "com.illusion.checkfirm"
    compileSdk = 36

    defaultConfig {
        applicationId = "com.illusion.checkfirm"
        minSdk = 28
        targetSdk = 36
        versionCode = 58
        versionName = "11.2.1"
    }

    signingConfigs {
        create("release") {
            keyAlias = keystoreProperties["keyAlias"] as String
            keyPassword = keystoreProperties["keyPassword"] as String
            storeFile = file(keystoreProperties["storeFile"] as String)
            storePassword = keystoreProperties["storePassword"] as String
        }
    }

    buildTypes {
        debug {
            isMinifyEnabled = true
            isShrinkResources = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
            signingConfig = signingConfigs.getByName("release")
        }
        release {
            isMinifyEnabled = true
            isShrinkResources = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
            signingConfig = signingConfigs.getByName("release")
        }
    }

    androidResources {
        generateLocaleConfig = true
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_21
        targetCompatibility = JavaVersion.VERSION_21
    }

    buildFeatures {
        compose = true
        buildConfig = true
    }

    packaging {
        resources {
            pickFirsts += arrayOf(
                "META-INF/LICENSE.md",
                "META-INF/NOTICE.md"
            )
        }
    }
}

kotlin {
    jvmToolchain(jdkVersion = 21)
}

room {
    schemaDirectory("$projectDir/schemas")
    generateKotlin = true
}

hilt {
    enableAggregatingTask = true
}

dependencies {
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.bundles.androidx.compose)
    implementation(libs.bundles.androidx.lifecycle)
    implementation(libs.bundles.androidx.navigation3)
    implementation(libs.bundles.angus)
    implementation(libs.bundles.data)
    implementation(libs.bundles.firebase)
    implementation(libs.bundles.hilt)
    implementation(libs.bundles.ktor)

    implementation(libs.androidx.core.ktx)
    implementation(libs.google.accompanist.permissions)
    implementation(libs.kotlinx.coroutine.android)
    implementation(libs.ksoup)

    ksp(libs.google.hilt.compiler)
    ksp(libs.kotlin.metadata.jvm)
    ksp(libs.room.compiler)

    debugImplementation(libs.androidx.compose.ui.tooling)
}