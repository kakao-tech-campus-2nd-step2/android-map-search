// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    id("com.android.application") version "8.3.1" apply false
    id("org.jetbrains.kotlin.android") version "1.9.0" apply false
    id("org.jlleitschuh.gradle.ktlint") version "12.1.0" apply false
    id("org.jetbrains.kotlin.kapt") version "1.9.0" apply false
}
// Add this if it's not present
subprojects {
    ext {
        set("KAKAO_REST_API_KEY", rootProject.findProperty("KAKAO_REST_API_KEY") as String)
    }
}

allprojects {
    apply(plugin = "org.jlleitschuh.gradle.ktlint")
}
