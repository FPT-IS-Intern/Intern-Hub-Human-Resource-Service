dependencies {
    // Project dependencies
    implementation(project(":infra"))
    implementation(project(":core"))

    // Custom Libraries
    implementation(libs.bundles.custom.libraries)

    // Spring Boot dependencies
    implementation(libs.spring.boot.starter.web)
    implementation(libs.spring.boot.starter.security)
    implementation(libs.openapi.doc)

    // MapStruct
    implementation(libs.mapstruct)
    annotationProcessor(libs.mapstruct.processor)

    implementation(libs.bundles.spring.boot.database)

    // Lombok
    compileOnly(libs.lombok)
    annotationProcessor(libs.lombok)
    annotationProcessor(libs.lombok.mapstruct.binding)
}

tasks.jar {
    enabled = false
}

tasks.bootJar {
    enabled = true
    archiveFileName.set("hrm-service.jar")
}

springBoot {
    mainClass.set("com.fis.hrmservice.api.InternHubHRMServiceApplication")
}
