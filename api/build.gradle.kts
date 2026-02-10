plugins {
    id("application")
}

springBoot {
    mainClass.set("com.fis.hrmservice.api.Main")
}

application {
    mainClass.set("com.fis.hrmservice.api.Main")
}

dependencies {
    // Project dependencies
    implementation(project(":infra"))
    implementation(project(":core"))
    implementation(project(":common"))

    // Custom libraries
    implementation(libs.bundles.custom.libraries)

    // Spring Boot
    implementation(libs.bundles.spring.boot.all)
    implementation(libs.spring.boot.starter.log4j2)
    implementation(libs.spring.boot.starter.data.jpa)
    implementation(libs.spring.boot.starter.security)

    // Documentation
    implementation(libs.openapi.doc)

    // MapStruct
    implementation(libs.mapstruct)
    annotationProcessor(libs.mapstruct.processor)
    annotationProcessor(libs.lombok.mapstruct.binding)

    // Lombok
    compileOnly(libs.lombok)
    annotationProcessor(libs.lombok)

    // Configuration processor
    annotationProcessor(libs.spring.boot.configuration.processor)
}

tasks.bootJar {
    enabled = true
    archiveFileName = "hrm-service.jar"
}