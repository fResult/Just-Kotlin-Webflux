plugins {
  kotlin("jvm") version "1.9.25"
  kotlin("plugin.spring") version "1.9.25"
  id("org.springframework.boot") version libs.versions.springboot.get()
  id("io.spring.dependency-management") version "1.1.7"
}

group = "dev.fResult"
version = "0.0.1"
description = "Working with Reactive Database"

java {
  toolchain {
    languageVersion = JavaLanguageVersion.of(21)
  }
}

repositories {
  mavenCentral()
}

dependencies {
  implementation(libs.spring.boot.starter.data.r2dbc)
  implementation(libs.spring.boot.starter.hateoas)
  implementation(libs.spring.boot.starter.thymeleaf)
  implementation(libs.spring.boot.starter.webflux)
  implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
  implementation("io.projectreactor.kotlin:reactor-kotlin-extensions")
  implementation("org.jetbrains.kotlin:kotlin-reflect")
  implementation("org.jetbrains.kotlinx:kotlinx-coroutines-reactor")
  implementation("io.projectreactor:reactor-test")

  developmentOnly(libs.spring.boot.devtools)

  runtimeOnly("com.h2database:h2")
  runtimeOnly("io.r2dbc:r2dbc-h2")

  testImplementation(libs.spring.boot.starter.test)
  testImplementation("org.jetbrains.kotlin:kotlin-test-junit5")
  testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}

kotlin {
  compilerOptions {
    freeCompilerArgs.addAll("-Xjsr305=strict")
  }
}

tasks.withType<Test> {
  useJUnitPlatform()
}
