dependencies {
    implementation(project(":core"))

    // Custom Libraries
    implementation(libs.bundles.custom.libraries)

    // Database (JPA + PostgreSQL + Liquibase)
    implementation(libs.bundles.spring.boot.database)

    // Feign client
    implementation(libs.spring.boot.starter.feign)
    implementation(libs.spring.boot.starter.validation)

    // AWS S3 SDK (Cloudflare R2)
    implementation(libs.s3.sdk)

    // MapStruct
    implementation(libs.mapstruct)
    annotationProcessor(libs.mapstruct.processor)

    // Lombok
    compileOnly(libs.lombok)
    annotationProcessor(libs.lombok)
    annotationProcessor(libs.lombok.mapstruct.binding)
}