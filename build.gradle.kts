import com.diffplug.gradle.spotless.SpotlessExtension
import io.spring.gradle.dependencymanagement.dsl.DependencyManagementExtension
import org.gradle.kotlin.dsl.configure

plugins {
    alias(libs.plugins.springBoot) apply false
    alias(libs.plugins.dependencyManagement) apply false
    alias(libs.plugins.spotless) apply false
    alias(libs.plugins.sonar) apply false
    alias(libs.plugins.jib) apply false
    java
}

allprojects {
    group = "fis.internhub"
    version = "0.0.1-SNAPSHOT"

    repositories {
        mavenCentral()
        maven { url = uri("https://jitpack.io") }
        maven { url = uri("https://jitpack.io") }
        mavenLocal()
    }
}
subprojects {

    apply(plugin = "java")
    apply(plugin = "io.spring.dependency-management")
    apply(plugin = "com.diffplug.spotless")
    apply(plugin = "org.sonarqube")

    java {
        toolchain {
            languageVersion.set(JavaLanguageVersion.of(25))
        }
    }

    configure<DependencyManagementExtension> {
        imports {
            mavenBom(
                "org.springframework.cloud:spring-cloud-dependencies:" +
                        rootProject.libs.versions.springCloud.get()
            )
        }
    }

    configurations.configureEach {
        exclude(group = "org.springframework.boot", module = "spring-boot-starter-logging")
        exclude(group = "ch.qos.logback", module = "logback-classic")
    }

    dependencies {
        // Spring Core
        implementation(rootProject.libs.spring.boot.starter)
        implementation(rootProject.libs.spring.boot.starter.log4j2)
        testImplementation(rootProject.libs.spring.boot.starter.test)

        // Utils
        implementation("cn.hutool:hutool-all:5.8.43")

        // MapStruct
        implementation("org.mapstruct:mapstruct:1.6.3")
        annotationProcessor("org.mapstruct:mapstruct-processor:1.6.3")

        // Lombok
        compileOnly("org.projectlombok:lombok:1.18.42")
        annotationProcessor("org.projectlombok:lombok:1.18.42")

        // Lombok + MapStruct binding
        annotationProcessor("org.projectlombok:lombok-mapstruct-binding:0.2.0")

        //common lib
        implementation("com.github.FPT-IS-Intern:Intern-Hub-Common-Library:2.0.0")

        //Security Starter Library
        implementation("com.github.FPT-IS-Intern:Intern-Hub-Security-Starter:1.0.0")
    }

    configure<SpotlessExtension> {
        java {
            googleJavaFormat(rootProject.libs.versions.googleJavaFormat.get())
            importOrder()
            removeUnusedImports()
            endWithNewline()
        }
    }

    tasks.withType<Test> {
        useJUnitPlatform()
    }

}