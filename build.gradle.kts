plugins {
    java
    id("org.springframework.boot") version "3.4.4"
    id("io.spring.dependency-management") version "1.1.7"
    id("org.sonarqube") version "5.0.0.4638"
    jacoco 
}

group = "id.ac.ui.cs.advprog"
version = "0.0.1-SNAPSHOT"

val javaToolchainVersion = 21
val dotenvJavaVersion = "3.0.0"
val postgresqlVersion = "42.7.3"
val jaxbApiVersion = "2.3.1"
val javassistVersion = "3.25.0-GA"
val lombokVersion = "1.18.34"
val h2databaseVersion = "2.2.224"
val springSecurityTestVersion="6.0.2"
val springSecurityCryptoVersion="6.0.2"
val jjwtVersion="0.11.5"

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(javaToolchainVersion)
    }
}

configurations {
    compileOnly {
        extendsFrom(configurations.annotationProcessor.get())
    }
}

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("io.github.cdimascio:dotenv-java:$dotenvJavaVersion")
    implementation("org.postgresql:postgresql:$postgresqlVersion")
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    implementation("javax.xml.bind:jaxb-api:$jaxbApiVersion")
    implementation("org.javassist:javassist:$javassistVersion")

    compileOnly("org.projectlombok:lombok:$lombokVersion")

    developmentOnly("org.springframework.boot:spring-boot-devtools")

    annotationProcessor("org.springframework.boot:spring-boot-configuration-processor")
    annotationProcessor("org.projectlombok:lombok:$lombokVersion")

    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("com.h2database:h2:$h2databaseVersion")

    testImplementation("org.springframework.security:spring-security-test:$springSecurityTestVersion")
    implementation("io.jsonwebtoken:jjwt-api:$jjwtVersion")
    runtimeOnly("io.jsonwebtoken:jjwt-impl:$jjwtVersion")
    runtimeOnly("io.jsonwebtoken:jjwt-jackson:$jjwtVersion")

    implementation("org.springframework.security:spring-security-crypto:$springSecurityCryptoVersion")
    implementation("org.springframework.boot:spring-boot-starter-security")
}

jacoco {
    toolVersion = "0.8.12" 
}

tasks.withType<Test> {
    useJUnitPlatform()
    finalizedBy(tasks.jacocoTestReport) 
}

tasks.jacocoTestReport {
    dependsOn(tasks.test) 
    reports {
        xml.required.set(true) 
        html.required.set(true) 
        csv.required.set(false)
    }
}
