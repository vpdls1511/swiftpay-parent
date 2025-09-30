plugins {
  kotlin("jvm") version "1.9.25"
  kotlin("plugin.spring") version "1.9.25"
  id("org.springframework.boot") version "3.5.6"
  id("io.spring.dependency-management") version "1.1.7"
}

group = "com.ngyu"
version = "0.0.1-SNAPSHOT"
description = "swiftpay-api"

java {
  toolchain {
    languageVersion = JavaLanguageVersion.of(17)
  }
}

tasks.jar {
  enabled = false
}

repositories {
  mavenCentral()
}

dependencies {
  implementation(project(":swiftpay-core"))
  implementation(project(":swiftpay-infrastructure"))

  implementation("org.springframework.boot:spring-boot-starter")
  implementation("org.jetbrains.kotlin:kotlin-reflect")
  implementation("org.springframework.boot:spring-boot-starter-web")
  implementation("org.springframework.boot:spring-boot-starter-actuator")

  testImplementation("org.springframework.boot:spring-boot-starter-test")

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
