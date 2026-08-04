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
