plugins {
    id("java-library")
}

dependencies {
    // Project dependencies
    implementation(project(":common"))

    // Custom libraries
    implementation(libs.bundles.custom.libraries)

    // Spring Core
    implementation(libs.spring.boot.starter)
    implementation(libs.spring.boot.starter.log4j2)
    implementation(libs.spring.boot.starter.security)

    // Spring Data JPA (for @Modifying annotation)
    implementation(libs.spring.boot.starter.data.jpa)

    // MapStruct
    implementation(libs.mapstruct)
    annotationProcessor(libs.mapstruct.processor)

    // Lombok
    compileOnly(libs.lombok)
    annotationProcessor(libs.lombok)
    annotationProcessor(libs.lombok.mapstruct.binding)
}