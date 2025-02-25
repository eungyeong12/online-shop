plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.google.gms.google.services)
}

android {
    namespace = "jo.onlineshop"
    compileSdk = 35

    defaultConfig {
        applicationId = "jo.onlineshop"
        minSdk = 24
        targetSdk = 34
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
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = "11"
    }
    buildFeatures {
        compose = true // Jetpack Compose 관련 기능과 라이브러리를 사용할 수 있도록 활성화
    }
    composeOptions {
        // Jetpack Compose 컴파일러 확장 버전을 설정
        kotlinCompilerExtensionVersion = "1.5.14"
    }
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.firebase.database)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    // Coil 라이브러리 - 네트워크 또는 로컬에서 이미지를 불러오고 표시하는 데 사용
    // kotlin 코루틴을 사용하여 비동기적으로 이미지 로딩을 최적화함
    implementation("io.coil-kt:coil-compose:2.2.2")
    // ViewPager(페이지 슬라이더) 사용 시 페이지 위치를 시각적으로 나타냄
    implementation("com.google.accompanist:accompanist-pager-indicators:0.28.0")
    // ViewPager 기능을 사용하도록 지원
    implementation("com.google.accompanist:accompanist-pager:0.28.0")
    // ViewModel, LiveData, Lifecycle 지원
    // Android에서 라이프사이클을 안전하게 관리하도록 도움.
    // 코루틴을 사용한 비동기 작업 지원
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.x.x")
    // ViewModel에서 제공하는 LiveData 값을 Compose UI에서 자동으로 업데이트
    implementation("androidx.compose.runtime:runtime-livedata:x.x.x")
    // 이미지 로딩 라이브러리
    implementation("com.github.bumptech.glide:glide:4.12.0")
    // JSON 데이터를 Kotlin 객체로 변환하거나, 반대로 객체를 JSON으로 변환
    implementation("com.google.code.gson:gson:2.9.1")
    // Jetpack Compose에서 ConstraintLayout을 사용할 수 있도록 지원
    implementation("androidx.constraintlayout:constraintlayout:2.2.0")
    implementation("androidx.constraintlayout:constraintlayout-compose:1.1.0")
    // Jetpack Compose에서 기본 UI 요소를 포함한 Foundation 레벨의 API 제공
    implementation("androidx.compose.foundation:foundation:1.5.0")
    // Jetpack Compose UI 디버깅 및 프리뷰 기능 제공
    implementation("androidx.compose.ui:ui-tooling:1.7.5")
    // Google Material Design 컴포넌트 제공
    implementation("androidx.compose.material3:material3:1.3.1")
    // Jetpack Compose를 Activity에서 사용할 수 있도록 함
    implementation("androidx.activity:activity-compose:1.9.2")
}