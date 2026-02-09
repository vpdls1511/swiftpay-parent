plugins {
  kotlin("jvm") version "1.9.25"
  kotlin("plugin.spring") version "1.9.25"
  kotlin("plugin.jpa") version "1.9.25"

  id("org.springframework.boot") version "3.5.6"
  id("io.spring.dependency-management") version "1.1.7"
}

group = "com.ngyu"
version = "0.0.1-SNAPSHOT"
description = "swiftpay-auth"

java {
  toolchain {
    languageVersion = JavaLanguageVersion.of(17)
  }
}

tasks.bootJar { // 실행 파일 안만듦
  enabled = false
}

tasks.jar { // 라이브러리용 파일은 만듦
  enabled = true
}

repositories {
  mavenCentral()
}

dependencies {
  implementation(project(":module:core"))
  implementation(project(":module:common"))

  implementation("org.springframework.boot:spring-boot-starter")
  implementation("org.springframework.boot:spring-boot-starter-web")
  implementation("org.jetbrains.kotlin:kotlin-reflect")

  api("org.springframework.boot:spring-boot-starter-security")

  api("org.springframework.boot:spring-boot-starter-data-jpa")
  runtimeOnly("com.mysql:mysql-connector-j")

  testImplementation("org.springframework.boot:spring-boot-starter-test")
  testImplementation("org.jetbrains.kotlin:kotlin-test-junit5")
  testRuntimeOnly("org.junit.platform:junit-platform-launcher")
  testImplementation("com.h2database:h2")
  testImplementation("io.mockk:mockk:1.13.8")
  testImplementation("org.assertj:assertj-core:3.24.2")
}

kotlin {
  compilerOptions {
    freeCompilerArgs.addAll("-Xjsr305=strict")
  }
}

tasks.withType<Test> {
  useJUnitPlatform()
}
