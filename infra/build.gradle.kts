plugins {
    id("java-library")
}

dependencies {
    // Project dependencies
    api(project(":core"))
    api(project(":common"))

    // Custom libraries
    implementation(libs.bundles.custom.libraries)

    // Spring Boot
    implementation(libs.spring.boot.starter.security)
    implementation(libs.bundles.spring.boot.database)
    implementation(libs.spring.boot.starter.validation)
    implementation(libs.spring.boot.starter)
    implementation(libs.spring.boot.starter.log4j2)
    implementation(libs.spring.boot.starter.liquibase)

    // Spring Cloud
    implementation(libs.spring.cloud.eureka)
    implementation(libs.spring.cloud.feign)

    // MapStruct
    implementation(libs.mapstruct)
    annotationProcessor(libs.mapstruct.processor)

    // Lombok
    compileOnly(libs.lombok)
    annotationProcessor(libs.lombok)
    annotationProcessor(libs.lombok.mapstruct.binding)
}