import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    id("org.springframework.boot") version "2.4.5"
    id("io.spring.dependency-management") version "1.0.11.RELEASE"
    kotlin("jvm") version "1.4.32"
    kotlin("plugin.spring") version "1.4.32"
    kotlin("plugin.jpa") version "1.4.32"
}

group = "me.hl"
version = "0.0.1-SNAPSHOT"
java.sourceCompatibility = JavaVersion.VERSION_11

repositories {
    mavenCentral()
}

extra["springCloudVersion"] = "2020.0.2"

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-actuator")
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    implementation("org.springframework.boot:spring-boot-starter-data-redis")
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter-validation")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
    implementation("org.jetbrains.kotlin:kotlin-reflect")
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
    implementation("org.springframework.cloud:spring-cloud-starter-sleuth")
    runtimeOnly("mysql:mysql-connector-java")
    runtimeOnly("com.h2database:h2")
    testImplementation("junit:junit")
    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("com.google.code.gson:gson:2.9.0")
}

dependencyManagement {
    imports {
        mavenBom("org.springframework.cloud:spring-cloud-dependencies:${property("springCloudVersion")}")
    }
}

configurations.all {
    resolutionStrategy {
        failOnVersionConflict()
        this.eachDependency {
            if (requested.group == "ch.qos.logback") {
                useVersion("[1.2.10, 1.3)")
                because(
                    "Fixes critical logback vulnerability in 1.2.8+, last version now 1.2.10. " +
                            "Limited to 1.3 because of a legacy alpha 1.3 version."
                )
            }
            if (requested.group == "org.apache.logging.log4j") {
                useVersion("[2.17.1,)")
                because("Fixes critical log4j vulnerability in 2.17.1")
            }
        }
    }
}

tasks.withType<KotlinCompile> {
    kotlinOptions {
        freeCompilerArgs = listOf("-Xjsr305=strict")
        jvmTarget = "11"
    }
}

tasks.withType<Test> {
    useJUnit()
}
