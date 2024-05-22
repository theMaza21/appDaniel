plugins {
    id("com.android.application")
    id("com.google.gms.google-services")
}

android {
    namespace = "com.daniel.appdaniel"
    compileSdk = 33

    defaultConfig {
        applicationId = "com.daniel.appdaniel"
        minSdk = 24
        targetSdk = 33
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
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
}

dependencies {
    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.google.android.material:material:1.8.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    implementation("io.github.florent37:shapeofview:1.4.7")
    implementation("de.hdodenhof:circleimageview:3.1.0")
    implementation(platform("com.google.firebase:firebase-bom:32.3.1"))
    implementation("com.google.firebase:firebase-auth:19.1.0")
    implementation ("com.google.firebase:firebase-firestore:21.2.1")
    implementation("com.google.android.gms:play-services-auth:20.4.1")
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
    implementation ("com.google.android.gms:play-services-auth:latest_version")
    implementation ("com.github.d-max:spots-dialog:1.1")
    implementation ("com.google.firebase:firebase-storage:19.1.1")
    implementation ("id.zelory:compressor:2.1.1")
    implementation ("com.squareup.picasso:picasso:2.8")
    implementation ("com.github.mancj:MaterialSearchBar:0.8.5")
    implementation("com.firebaseui:firebase-ui-firestore:6.2.1")
    implementation ("com.github.smarteist:autoimageslider:1.3.7")
    implementation ("com.google.firebase:firebase-messaging:23.2.1")
    implementation("com.google.firebase:firebase-analytics")
    implementation("com.google.code.gson:gson:2.10.1")
    implementation("com.squareup.retrofit2:converter-gson:2.4.0")
    implementation("com.squareup.retrofit2:converter-scalars:2.4.0")
    implementation("com.squareup.retrofit2:retrofit:2.4.0")


}

