plugins {
    id("java-library")
}

dependencies {
    // Spring Core
    implementation(libs.spring.boot.starter)
    implementation(libs.spring.boot.starter.log4j2)

    // Utils
    implementation(libs.jakarta.validation)

    // Lombok
    compileOnly(libs.lombok)
    annotationProcessor(libs.lombok)
}
