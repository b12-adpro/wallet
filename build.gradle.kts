plugins {
    java
    id("org.springframework.boot") version "3.4.4"
    id("io.spring.dependency-management") version "1.1.7"
    id("org.sonarqube") version "5.0.0.4638"
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
}

tasks.withType<Test> {
    useJUnitPlatform()
}