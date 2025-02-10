plugins {
  kotlin("jvm") version "1.9.25"
  kotlin("plugin.spring") version "1.9.25"
  id("org.springframework.boot") version libs.versions.springboot.get()
  id("io.spring.dependency-management") version "1.1.7"
}

group = "dev.fResult"
version = "0.0.1"
description = "Practicing Spring Security with Kotlin"

java {
  toolchain {
    languageVersion = JavaLanguageVersion.of(21)
  }
}

subprojects {
  apply(plugin = "org.springframework.boot")
  apply(plugin = "io.spring.dependency-management")
  apply(plugin = "kotlin")
  apply(plugin = "kotlin-spring")

  dependencies {
    implementation("org.springframework.boot:spring-boot-starter")
    implementation("org.jetbrains.kotlin:kotlin-reflect")
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
    testImplementation("org.springframework.boot:spring-boot-starter-test")
  }

  tasks.withType<Test> {
    useJUnitPlatform()
  }
}

repositories {
  mavenCentral()
}

dependencies {
  developmentOnly(libs.spring.boot.devtools)
}
