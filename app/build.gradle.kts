pluginManagement {
repositories {
google();
mavenCentral();
gradlePluginPortal();
}
}

dependencyResolutionManagement {
repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS);
repositories {
google();
mavenCentral();
}
}
rootProject.name = &quot;tetris-app&quot;;
include(&quot;:app&quot;);
================================================================================
فایل ۳: app/build.gradle.kts (داخل پوشه app)
مسیر: tetris-app/app/build.gradle.kts
توضیح: تنظیمات کامل بیلد اندروید، ورژن Compose و وابستگی‌ها (Dependencies)
================================================================================
plugins {
id(&quot;com.android.application&quot;);
id(&quot;org.jetbrains.kotlin.android&quot;);
}
android {
namespace = &quot;com.example.tetris&quot;;
compileSdk = 34;
defaultConfig {
applicationId = &quot;com.example.tetris&quot;;
minSdk = 24;
targetSdk = 34;
versionCode = 1;
versionName = &quot;1.0&quot;;
}
buildTypes {
release {
isMinifyEnabled = false;
}
}
compileOptions {
sourceCompatibility = JavaVersion.VERSION_17;
targetCompatibility = JavaVersion.VERSION_17;
}
kotlinOptions {
jvmTarget = &quot;17&quot;;
}
buildFeatures {
compose = true;
}
composeOptions {
kotlinCompilerExtensionVersion = &quot;1.5.8&quot;;
}

}
dependencies {
implementation(&quot;androidx.core:core-ktx:1.12.0&quot;);
implementation(&quot;androidx.lifecycle:lifecycle-runtime-ktx:2.7.0&quot;);
implementation(&quot;androidx.activity:activity-compose:1.8.2&quot;);
implementation(platform(&quot;androidx.compose:compose-bom:2024.02.00&quot;));
implementation(&quot;androidx.compose.ui:ui&quot;);
implementation(&quot;androidx.compose.ui:ui-graphics&quot;);
implementation(&quot;androidx.compose.ui:ui-tooling-preview&quot;);
implementation(&quot;androidx.compose.material3:material3&quot;);
}
