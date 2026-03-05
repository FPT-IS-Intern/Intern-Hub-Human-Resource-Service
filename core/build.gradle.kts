dependencies {
    // Custom Libraries (common-library: exceptions, Snowflake, PaginatedData; security-starter: annotations)
    implementation(libs.bundles.custom.libraries)

    // Spring Web (MultipartFile) + JPA (Transactional) + Validation
    implementation(libs.spring.boot.starter.web)
    implementation(libs.spring.boot.starter.data.jpa)
    implementation(libs.spring.boot.starter.validation)

    // Lombok
    compileOnly(libs.lombok)
    annotationProcessor(libs.lombok)
}