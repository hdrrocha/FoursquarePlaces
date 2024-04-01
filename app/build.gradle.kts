plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
}
android {
    namespace = "com.example.foursquareplaces"
    compileSdk = 34

    buildFeatures {
        buildConfig = true
    }

    defaultConfig {
        applicationId = "com.example.foursquareplaces"
        minSdk = 21
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        buildConfigField("String", "BASE_URL", "\"https://api.foursquare.com/v3/\"")
        buildConfigField("String", "AUTH_TOKEN", "\"fsq3ow/gIdM/fNWUagLZyT3WwnM0vVzh6Zcvzy+ADDQ2JLo=\"")
        buildConfigField("String", "SEARCH_FIELDS", "\"name,price,distance,rating,location,photos,fsq_id\"")
        buildConfigField("String", "DETAILS_FIELDS", "\"name,categories,price,rating,distance,tel,location,hours,photos,tips\"")
        buildConfigField("String", "CLIENT_ID", "\"1YM3HHS4WK5OCUGHFXDNV0AD4PSPV0ODDZSGII4J0IXIZT4V\"")
        buildConfigField("String", "CLIENT_SECRET", "\"03E0MTZ5OK4RETVJP332C3Q3H2ELSHFH4XKCVAH3HZCKKCWC\"")
        buildConfigField("String", "VERSION_API", "\"20240126\"")

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
    flavorDimensions("version")
    productFlavors {
        create("coffee") {
            dimension = "version"
            applicationId = "com.example.foursquarecoffeeplaces"
            versionCode = 1
            versionName = "1.0.0"
            buildConfigField("String", "FLAVOR_SELECTED", "\"coffee\"")
            buildConfigField("String", "RADIUS", "\"1000\"")
        }

        create("drinks") {
            dimension = "version"
            applicationId = "com.example.foursquaredrinksplaces"
            versionCode = 1
            versionName = "1.0.0"
            buildConfigField("String", "FLAVOR_SELECTED", "\"drink\"")
            buildConfigField("String", "RADIUS", "\"3000\"")
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
        kotlinCompilerExtensionVersion = "1.5.1"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}


dependencies {

    // Android Jetpack Libraries
    implementation("androidx.core:core-ktx:1.9.0") // Kotlin Extensions for Android APIs
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.6.2") // Android Lifecycle Components
    implementation("androidx.activity:activity-compose:1.3.1") // Compose Integration with Activity
    implementation(platform("androidx.compose:compose-bom:2023.03.00")) // Compose BOM (Bill of Materials)
    implementation("androidx.compose.ui:ui") // Core Compose library
    implementation("androidx.compose.ui:ui-graphics") // Compose Graphics Resources
    implementation("androidx.compose.ui:ui-tooling-preview") // Compose Preview Tooling
    implementation("androidx.compose.material3:material3") // Material Design 3 for Compose
    implementation("androidx.compose.material:material:1.0.0") // Material Design Library for Compose
    implementation("androidx.compose.ui:ui:1.0.0") // Core Compose library
    implementation("androidx.compose.material:material-icons-core:1.0.0") // Material Design Icons for Compose
    implementation("androidx.compose.material:material-icons-extended:1.0.0") // Extended Material Design Icons for Compose
    implementation("androidx.navigation:navigation-compose:2.7.7") // Compose Navigation Library
    implementation("com.google.android.gms:play-services-location:21.2.0")
    implementation("androidx.compose.ui:ui-util:1.1.0")
    implementation("androidx.viewpager2:viewpager2:1.0.0")

    testImplementation("junit:junit:4.13.2") // JUnit Testing Framework for Java
    testImplementation("org.mockito:mockito-core:3.11.2")
    testImplementation("org.mockito:mockito-inline:3.12.4") 
    androidTestImplementation("androidx.test.ext:junit:1.1.5") // JUnit Extension for Android Instrumentation Testing
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1") // UI Testing Framework for Android
    androidTestImplementation(platform("androidx.compose:compose-bom:2023.03.00")) // Compose BOM for Instrumentation Testing
    androidTestImplementation("androidx.compose.ui:ui-test-junit4") // JUnit Tests for Compose UI
    androidTestImplementation( "androidx.test:rules1.3.0") // JUnit Tests for Compose UI
    androidTestImplementation( "androidx.test.ext:truth:1.2.0") // JUnit Tests for Compose UI
    androidTestImplementation( "com.squareup.okhttp3:mockwebserver:4.1.0") // JUnit Tests for Compose UI

    debugImplementation("androidx.compose.ui:ui-tooling") // Compose Development Tools
    debugImplementation("androidx.compose.ui:ui-test-manifest") // Manifest for Compose Instrumentation Testing
    debugImplementation("androidx.compose.runtime:runtime-livedata:1.0.5") // LiveData for Compose

//    androidTestImplementation "androidx.test.ext:junit:$versions.junit_x_ext"
//    androidTestImplementation "androidx.test:rules:$versions.testx_rules"
//
//    androidTestImplementation 'androidx.test.espresso:espresso-core:3.4.0'
//    androidTestImplementation 'androidx.test.uiautomator:uiautomator:2.2.0'
//    androidTestImplementation "androidx.test.ext:truth:$versions.truthVersion"
//    androidTestImplementation 'androidx.test:rules:1.2.0'

    // Glide for image loading
    implementation("com.github.bumptech.glide:glide:4.12.0")
    implementation("com.github.bumptech.glide:compiler:4.12.0")

    // Retrofit for network communication
    implementation("com.squareup.retrofit2:retrofit:2.9.0")
    implementation("com.squareup.retrofit2:converter-gson:2.9.0")
    implementation("com.squareup.okhttp3:okhttp:4.9.2")
    implementation("com.squareup.okhttp3:logging-interceptor:4.9.1")

    // Moshi for JSON processing
    implementation("com.squareup.moshi:moshi-kotlin:1.15.1")

    // Koin for dependency injection
    implementation("io.insert-koin:koin-android:3.5.3")
    implementation("io.insert-koin:koin-androidx-compose:3.5.0")

}
