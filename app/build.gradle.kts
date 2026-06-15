import java.util.Properties

plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("org.jetbrains.kotlin.plugin.compose")
    id("com.google.devtools.ksp")
}

val localProperties = Properties().apply {
    val localPropertiesFile = rootProject.file("local.properties")
    if (localPropertiesFile.isFile) {
        localPropertiesFile.inputStream().use { stream -> load(stream) }
    }
}

fun String.asBuildConfigString(): String =
    "\"" + replace("\\", "\\\\").replace("\"", "\\\"") + "\""

android {
    namespace = "com.boldrex.postavki"
    compileSdk = 36

    defaultConfig {
        applicationId = "com.boldrex.postavki"
        minSdk = 29
        targetSdk = 36
        versionCode = 10
        versionName = "1.9"

        buildConfigField(
            "String",
            "OZON_CLIENT_ID",
            (localProperties.getProperty("ozon.clientId") ?: System.getenv("OZON_CLIENT_ID") ?: "")
                .trim()
                .asBuildConfigString()
        )
        buildConfigField(
            "String",
            "OZON_API_KEY",
            (localProperties.getProperty("ozon.apiKey") ?: System.getenv("OZON_API_KEY") ?: "")
                .trim()
                .asBuildConfigString()
        )
        buildConfigField(
            "String",
            "UPDATE_MANIFEST_URL",
            (localProperties.getProperty("update.manifestUrl") ?: System.getenv("UPDATE_MANIFEST_URL") ?: "http://192.168.0.105:8088/manifest.json")
                .trim()
                .asBuildConfigString()
        )
    }

    buildFeatures {
        compose = true
        buildConfig = true
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
}

kotlin {
    compilerOptions {
        jvmTarget.set(org.jetbrains.kotlin.gradle.dsl.JvmTarget.JVM_17)
    }
}

tasks.register("wrapper") {
    group = "Build Setup"
    description = "Delegates to the root Gradle wrapper task when Android Studio runs :app:wrapper."
    dependsOn(rootProject.tasks.named("wrapper"))
}

tasks.register("prepareKotlinBuildScriptModel") {
    group = "Build Setup"
    description = "Delegates to the root Kotlin build script model task when Android Studio runs :app:prepareKotlinBuildScriptModel."
    dependsOn(rootProject.tasks.named("prepareKotlinBuildScriptModel"))
}

dependencies {
    implementation(platform("androidx.compose:compose-bom:2026.03.00"))
    implementation("androidx.activity:activity-compose:1.13.0")
    implementation("androidx.compose.ui:ui")
    implementation("androidx.compose.ui:ui-tooling-preview")
    implementation("androidx.compose.material3:material3")
    implementation("androidx.compose.material:material-icons-extended")
    debugImplementation("androidx.compose.ui:ui-tooling")

    implementation("androidx.core:core-ktx:1.18.0")
    implementation("androidx.lifecycle:lifecycle-runtime-compose:2.10.0")
    implementation("androidx.lifecycle:lifecycle-viewmodel-compose:2.10.0")
    implementation("androidx.work:work-runtime-ktx:2.11.2")

    implementation("androidx.room:room-runtime:2.8.4")
    implementation("androidx.room:room-ktx:2.8.4")
    ksp("androidx.room:room-compiler:2.8.4")

    implementation("androidx.camera:camera-core:1.6.0")
    implementation("androidx.camera:camera-camera2:1.6.0")
    implementation("androidx.camera:camera-lifecycle:1.6.0")
    implementation("androidx.camera:camera-view:1.6.0")
    implementation("com.google.guava:guava:33.4.8-android")
    implementation("com.google.mlkit:barcode-scanning:17.3.0")
    implementation("eu.agno3.jcifs:jcifs-ng:2.1.10")

    testImplementation("junit:junit:4.13.2")
    testImplementation("org.json:json:20260522")
    androidTestImplementation("androidx.test.ext:junit:1.3.0")
    androidTestImplementation("androidx.test:core-ktx:1.7.0")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.7.0")

}
